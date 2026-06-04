import React, { useState, useEffect } from 'react';
import StatusAlert from '../components/StatusAlert';

const Dashboard = () => {
  const [donaciones, setDonaciones] = useState([]);
  const [logistica, setLogistica] = useState([]);
  const [necesidades, setNecesidades] = useState([]);

  // Estados para controlar qué modal está abierto
  const [modalDonacion, setModalDonacion] = useState(false);
  const [modalVerDespachos, setModalVerDespachos] = useState(false);
  const [modalNecesidad, setModalNecesidad] = useState(false);

  // Estados para los formularios
  const [formDonacion, setFormDonacion] = useState({ donante: '', tipoAyuda: '', cantidad: 1 });
  const [formNecesidad, setFormNecesidad] = useState({ descripcion: '', cantidadRequerida: 1, prioridad: 'MEDIA', institucionSolicitante: '' });

  // Función unificada para refrescar los datos desde el BFF
  const cargarDatos = () => {
    fetch('http://localhost:8080/api/bff/donaciones').then(res => res.json()).then(data => setDonaciones(data)).catch(() => setDonaciones(null));
    fetch('http://localhost:8080/api/bff/logistica').then(res => res.json()).then(data => setLogistica(data)).catch(() => setLogistica(null));
    fetch('http://localhost:8080/api/bff/necesidades').then(res => res.json()).then(data => setNecesidades(data)).catch(() => setNecesidades(null));
  };

  useEffect(() => {
    cargarDatos();
  }, []);

  // 📥 POST: Registrar Donación (Corregido con la estructura exacta de Donacion.java)
  const manejarDonacionSubmit = (e) => {
    e.preventDefault();
    
    // Armamos el objeto con las llaves exactas que espera tu @Entity de Java
    const payloadDonacion = {
      donante: formDonacion.donante,
      tipoRecurso: formDonacion.tipoAyuda, // ◄ ¡Aquí estaba el truco! De 'tipoAyuda' a 'tipoRecurso'
      cantidad: parseFloat(formDonacion.cantidad) // Aseguramos que viaje como Double/Number
    };

    fetch('http://localhost:8080/api/bff/donaciones', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payloadDonacion)
    })
    .then(res => {
      if (res.ok) {
        setModalDonacion(false);
        setFormDonacion({ donante: '', tipoAyuda: '', cantidad: 1 });
        cargarDatos(); // Refrescar contadores en pantalla
      } else {
        console.error("El BFF rechazó la petición. Revisa la consola de Spring Boot.");
      }
    })
    .catch(err => console.error("Error de red:", err));
  };

  // 📋 POST: Registrar Necesidad (Estructura exacta de Necesidad.java)
  const manejarNecesidadSubmit = (e) => {
    e.preventDefault();
    fetch('http://localhost:8080/api/bff/necesidades', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formNecesidad)
    })
    .then(() => {
      setModalNecesidad(false);
      setFormNecesidad({ descripcion: '', cantidadRequerida: 1, prioridad: 'MEDIA', institucionSolicitante: '' });
      cargarDatos(); // Refrescar contadores
    });
  };

  return (
    <div className="dashboard-container">
      <h2>Panel de Control Operativo</h2>
      <p style={{ color: '#6b7280', marginTop: 0, marginBottom: '2rem' }}>
        Monitoreo unificado de la red de ayuda humanitaria mediante microservicios reactivos.
      </p>
      
      <div className="grid-modules">
        {/* MÓDULO 1: DONACIONES */}
        <div className="card">
          <h3 style={{ marginTop: 0, color: '#1e3a8a' }}>🎁 Gestión de Donaciones</h3>
          <StatusAlert data={donaciones} defaultMessage="Servicio core de donaciones no disponible." />
          {Array.isArray(donaciones) ? (
            <p>Se encuentran <strong>{donaciones.length}</strong> donaciones procesadas en la base de datos.</p>
          ) : (
            <p style={{ color: '#9ca3af', fontStyle: 'italic' }}>Información temporalmente restringida.</p>
          )}
          <button className="btn-primary" onClick={() => setModalDonacion(true)} disabled={!Array.isArray(donaciones)}>
            Registrar Aporte
          </button>
        </div>

        {/* MÓDULO 2: LOGÍSTICA */}
        <div className="card">
          <h3 style={{ marginTop: 0, color: '#1e3a8a' }}>🚚 Despacho y Logística</h3>
          <StatusAlert data={logistica} defaultMessage="Servicio de seguimiento logístico fuera de línea." />
          {Array.isArray(logistica) ? (
            <p>Hay <strong>{logistica.length}</strong> rutas de transporte activas hacia los albergues.</p>
          ) : (
            <p style={{ color: '#9ca3af', fontStyle: 'italic' }}>Monitoreo en modo degradado.</p>
          )}
          <button className="btn-primary" style={{ backgroundColor: '#1e3a8a' }} onClick={() => setModalVerDespachos(true)} disabled={!Array.isArray(logistica)}>
            Ver Despachos
          </button>
        </div>

        {/* MÓDULO 3: NECESIDADES */}
        <div className="card">
          <h3 style={{ marginTop: 0, color: '#1e3a8a' }}>📋 Catastro de Necesidades</h3>
          <StatusAlert data={necesidades} defaultMessage="Servicio de requerimientos no disponible." />
          {Array.isArray(necesidades) ? (
            <p>Existen <strong>{necesidades.length}</strong> solicitudes críticas levantadas en terreno.</p>
          ) : (
            <p style={{ color: '#9ca3af', fontStyle: 'italic' }}>Catastro fuera de servicio.</p>
          )}
          <button className="btn-primary" style={{ backgroundColor: '#4b5563' }} onClick={() => setModalNecesidad(true)} disabled={!Array.isArray(necesidades)}>
            Nueva Solicitud
          </button>
        </div>
      </div>

      {/* ================= MODAL: REGISTRAR APORTE ================= */}
      {modalDonacion && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>🎁 Registrar Nueva Donación</h3>
            <form onSubmit={manejarDonacionSubmit}>
              <div className="form-group">
                <label>Nombre del Donante / Organización</label>
                <input type="text" className="form-control" required value={formDonacion.donante} onChange={e => setFormDonacion({...formDonacion, donante: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Tipo de Ayuda (Insumo)</label>
                <input type="text" className="form-control" placeholder="Ej: Agua, Frazadas" required value={formDonacion.tipoAyuda} onChange={e => setFormDonacion({...formDonacion, tipoAyuda: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Cantidad</label>
                <input type="number" className="form-control" min="1" required value={formDonacion.cantidad} onChange={e => setFormDonacion({...formDonacion, cantidad: parseInt(e.target.value)})} />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn-secondary" onClick={() => setModalDonacion(false)}>Cancelar</button>
                <button type="submit" className="btn-primary" style={{ marginTop: 0 }}>Guardar en DB</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* ================= MODAL: VER DESPACHOS (TABLA LOGÍSTICA) ================= */}
      {modalVerDespachos && (
        <div className="modal-overlay">
          <div className="modal-content modal-content-large">
            <h3>🚚 Hoja de Ruta - Despachos Activos</h3>
            {Array.isArray(logistica) && logistica.length > 0 ? (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>ID Donación</th>
                    <th>Destino</th>
                    <th>Transportista</th>
                    <th>Estado de Envío</th>
                  </tr>
                </thead>
                <tbody>
                  {logistica.map((envio, idx) => (
                    <tr key={idx}>
                      <td>{envio.donacionId}</td>
                      <td>{envio.destino}</td>
                      <td>{envio.transportista}</td>
                      <td><span style={{ fontWeight: 'bold', color: '#10b981' }}>{envio.estadoEnvio}</span></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p>No existen despachos registrados en tránsito actualmente.</p>
            )}
            <button className="btn-secondary" style={{ marginTop: '1.5rem' }} onClick={() => setModalVerDespachos(false)}>Cerrar Ventana</button>
          </div>
        </div>
      )}

      {/* ================= MODAL: NUEVA SOLICITUD (NECESIDADES) ================= */}
      {modalNecesidad && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>📋 Levantar Requerimiento de Emergencia</h3>
            <form onSubmit={manejarNecesidadSubmit}>
              <div className="form-group">
                <label>Descripción del Requerimiento</label>
                <input type="text" className="form-control" placeholder="Ej: Medicamentos, Herramientas" required value={formNecesidad.descripcion} onChange={e => setFormNecesidad({...formNecesidad, descripcion: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Cantidad Requerida</label>
                <input type="number" className="form-control" min="1" required value={formNecesidad.cantidadRequerida} onChange={e => setFormNecesidad({...formNecesidad, cantidadRequerida: parseInt(e.target.value)})} />
              </div>
              <div className="form-group">
                <label>Prioridad de Urgencia</label>
                <select className="form-control" value={formNecesidad.prioridad} onChange={e => setFormNecesidad({...formNecesidad, prioridad: e.target.value})}>
                  <option value="ALTA">ALTA</option>
                  <option value="MEDIA">MEDIA</option>
                  <option value="BAJA">BAJA</option>
                </select>
              </div>
              <div className="form-group">
                <label>Institución / Albergue Solicitante</label>
                <input type="text" className="form-control" placeholder="Ej: Albergue Central, Municipalidad" required value={formNecesidad.institucionSolicitante} onChange={e => setFormNecesidad({...formNecesidad, institucionSolicitante: e.target.value})} />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn-secondary" onClick={() => setModalNecesidad(false)}>Cancelar</button>
                <button type="submit" className="btn-primary" style={{ marginTop: 0, backgroundColor: '#4b5563' }}>Enviar Solicitud</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;