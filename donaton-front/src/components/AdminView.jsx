import React from 'react';
import StatusAlert from './StatusAlert';

const AdminView = ({
  campanas,
  donaciones,
  formCampana,
  setFormCampana,
  manejarCampanaSubmit,
  cambiarEstadoCampana,
  cambiarEstadoDonacion,
  manejarCampanaDelete,
  manejarCampanaUpdate,
  editandoCampanaId,
  setEditandoCampanaId,
  formEditCampana,
  setFormEditCampana,
  styles,
  onLogout,
  darkMode
}) => {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '2.5rem' }}>
      
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h3 style={{ ...styles.cardTitle, margin: 0, borderBottom: 'none', paddingBottom: 0 }}>Panel de Administración</h3>
        <button
          onClick={onLogout}
          style={{
            ...styles.buttonAction,
            backgroundColor: darkMode ? 'rgba(242, 100, 48, 0.15)' : 'rgba(242, 100, 48, 0.05)',
            borderColor: '#F26430',
            color: '#F26430',
            width: 'auto'
          }}
        >
          Cerrar Sesión 🚪
        </button>
      </div>

      <div style={styles.card}>
        <h3 style={styles.cardTitle}>Crear Nueva Campaña de Ayuda</h3>
        <form onSubmit={manejarCampanaSubmit} style={{ display: 'flex', gap: '1.5rem', alignItems: 'flex-end', flexWrap: 'wrap' }}>
          <div style={{ flex: '1 1 250px', ...styles.formGroup, marginBottom: 0 }}>
            <label style={styles.label}>Nombre de la Campaña *</label>
            <input
              type="text"
              style={styles.input}
              required
              placeholder="Ej: Campaña Frazadas Invierno"
              value={formCampana.nombre}
              onChange={e => setFormCampana({ ...formCampana, nombre: e.target.value })}
            />
          </div>
          <div style={{ flex: '2 1 300px', ...styles.formGroup, marginBottom: 0 }}>
            <label style={styles.label}>Descripción / Fines operacionales *</label>
            <input
              type="text"
              style={styles.input}
              required
              placeholder="Detalles de recolección y logística..."
              value={formCampana.descripcion}
              onChange={e => setFormCampana({ ...formCampana, descripcion: e.target.value })}
            />
          </div>
          <button type="submit" style={{ ...styles.buttonPrimary, flex: '0 0 auto', width: 'auto', padding: '0.85rem 2rem' }}>
            Dar de Alta
          </button>
        </form>
      </div>

      <div style={styles.card}>
        <h3 style={styles.cardTitle}>Gestión de Campañas</h3>
        
        <StatusAlert data={campanas.length > 0 ? campanas : null} defaultMessage="No se pudieron cargar las campañas. El servicio de campañas podría estar fuera de línea." />

        <div style={{ overflowX: 'auto' }}>
          {Array.isArray(campanas) && campanas.length > 0 ? (
            <table style={styles.table}>
              <thead>
                <tr>
                  <th style={styles.th}>ID</th>
                  <th style={styles.th}>Nombre</th>
                  <th style={styles.th}>Descripción</th>
                  <th style={styles.th}>Estado Actual</th>
                  <th style={styles.th}>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {campanas.map((c, idx) => (
                  <tr key={c.id} style={{ backgroundColor: darkMode && idx % 2 === 0 ? 'rgba(255,255,255,0.02)' : 'transparent', transition: 'all 0.3s ease' }}>
                    <td style={styles.td}>{c.id}</td>
                    
                    {editandoCampanaId === c.id ? (
                      <>
                        <td style={styles.td}>
                          <input
                            type="text"
                            style={styles.input}
                            value={formEditCampana.nombre}
                            onChange={(e) => setFormEditCampana({ ...formEditCampana, nombre: e.target.value })}
                          />
                        </td>
                        <td style={styles.td}>
                          <input
                            type="text"
                            style={styles.input}
                            value={formEditCampana.descripcion}
                            onChange={(e) => setFormEditCampana({ ...formEditCampana, descripcion: e.target.value })}
                          />
                        </td>
                      </>
                    ) : (
                      <>
                        <td style={{ ...styles.td, fontWeight: '700', color: darkMode ? '#FFFFFF' : '#2A2D34' }}>{c.nombre}</td>
                        <td style={styles.td}>{c.descripcion}</td>
                      </>
                    )}

                    <td style={styles.td}>
                      <span style={{
                        padding: '0.35rem 0.75rem',
                        borderRadius: '20px',
                        fontSize: '0.8rem',
                        fontWeight: '700',
                        backgroundColor: c.estado === 'ACTIVE' ? 'rgba(0, 157, 220, 0.12)' : c.estado === 'PLANNED' ? 'rgba(242, 100, 48, 0.1)' : c.estado === 'COMPLETED' ? 'rgba(0, 157, 220, 0.08)' : 'rgba(242, 100, 48, 0.08)',
                        color: c.estado === 'ACTIVE' ? '#009DDC' : c.estado === 'PLANNED' ? '#F26430' : c.estado === 'COMPLETED' ? '#009DDC' : '#F26430',
                        border: `1px solid ${c.estado === 'ACTIVE' ? 'rgba(0, 157, 220, 0.35)' : c.estado === 'PLANNED' ? 'rgba(242, 100, 48, 0.35)' : c.estado === 'COMPLETED' ? 'rgba(0, 157, 220, 0.2)' : 'rgba(242, 100, 48, 0.2)'}`
                      }}>
                        {c.estado}
                      </span>
                    </td>
                    <td style={styles.td}>
                      <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                        {editandoCampanaId === c.id ? (
                          <>
                            <button style={styles.buttonPrimary} onClick={(e) => manejarCampanaUpdate(e)}>
                              Guardar
                            </button>
                            <button style={{ ...styles.buttonAction, backgroundColor: darkMode ? 'rgba(255, 255, 255, 0.1)' : 'rgba(42, 45, 52, 0.1)', color: darkMode ? '#FFFFFF' : '#2A2D34', borderColor: darkMode ? 'rgba(255,255,255,0.3)' : 'rgba(42,45,52,0.3)' }} onClick={() => setEditandoCampanaId(null)}>
                              Cancelar
                            </button>
                          </>
                        ) : (
                          <>
                            <button style={{ ...styles.buttonAction, backgroundColor: darkMode ? 'rgba(255, 255, 255, 0.1)' : 'rgba(42, 45, 52, 0.1)', color: darkMode ? '#FFFFFF' : '#2A2D34', borderColor: darkMode ? 'rgba(255,255,255,0.3)' : 'rgba(42,45,52,0.3)' }} onClick={() => { setEditandoCampanaId(c.id); setFormEditCampana({ nombre: c.nombre, descripcion: c.descripcion }); }}>
                              ✏️ Editar
                            </button>
                            <button style={{ ...styles.buttonAction, backgroundColor: darkMode ? 'rgba(242, 100, 48, 0.15)' : 'rgba(242, 100, 48, 0.05)', border: '1.5px solid #F26430', color: '#F26430' }} onClick={() => manejarCampanaDelete(c.id)}>
                              🗑️ Eliminar
                            </button>
                            {c.estado === 'PLANNED' && (
                              <button style={styles.buttonAction} onClick={() => cambiarEstadoCampana(c.id, 'ACTIVE')}>
                                Activar
                              </button>
                            )}
                            {c.estado === 'ACTIVE' && (
                              <button style={styles.buttonAction} onClick={() => cambiarEstadoCampana(c.id, 'COMPLETED')}>
                                Completar
                              </button>
                            )}
                            {c.estado !== 'CANCELLED' && c.estado !== 'COMPLETED' && (
                              <button style={{ ...styles.buttonAction, backgroundColor: darkMode ? 'rgba(242, 100, 48, 0.15)' : 'rgba(242, 100, 48, 0.05)', border: '1.5px solid #F26430', color: '#F26430' }} onClick={() => cambiarEstadoCampana(c.id, 'CANCELLED')}>
                                Cancelar
                              </button>
                            )}
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p style={{ fontStyle: 'italic', color: '#009DDC', textAlign: 'center', padding: '2rem 0' }}>No hay campañas registradas en el sistema o servicio no disponible.</p>
          )}
        </div>
      </div>

      <div style={styles.card}>
        <h3 style={styles.cardTitle}>Control Logístico de Donaciones</h3>
        
        <StatusAlert data={donaciones.length > 0 ? donaciones : null} defaultMessage="No se pudieron cargar las donaciones. El servicio de donaciones podría estar fuera de línea." />

        <div style={{ overflowX: 'auto' }}>
          {Array.isArray(donaciones) && donaciones.length > 0 ? (
            <table style={styles.table}>
              <thead>
                <tr>
                  <th style={styles.th}>ID</th>
                  <th style={styles.th}>Campaña ID</th>
                  <th style={styles.th}>Donante</th>
                  <th style={styles.th}>Recurso</th>
                  <th style={styles.th}>Cantidad</th>
                  <th style={styles.th}>Estado Logístico</th>
                  <th style={styles.th}>Gestión</th>
                </tr>
              </thead>
              <tbody>
                {donaciones.map((d, idx) => (
                  <tr key={d.id} style={{ backgroundColor: darkMode && idx % 2 === 0 ? 'rgba(255,255,255,0.02)' : 'transparent', transition: 'all 0.3s ease' }}>
                    <td style={styles.td}>{d.id}</td>
                    <td style={{ ...styles.td, fontWeight: '700', color: darkMode ? '#FFFFFF' : '#2A2D34' }}>#{d.campanaId}</td>
                    <td style={styles.td}>{d.donante || 'Anónimo'}</td>
                    <td style={styles.td}>{d.tipoRecurso}</td>
                    <td style={{ ...styles.td, fontWeight: '700', color: '#009DDC' }}>{d.cantidad}</td>
                    <td style={styles.td}>
                      <span style={{
                        padding: '0.35rem 0.75rem',
                        borderRadius: '20px',
                        fontSize: '0.8rem',
                        fontWeight: '700',
                        backgroundColor: d.estadoLogistico === 'ENTREGADO' ? 'rgba(0, 157, 220, 0.12)' : d.estadoLogistico === 'EN_TRANSITO' ? 'rgba(242, 100, 48, 0.1)' : (darkMode ? 'rgba(255, 255, 255, 0.08)' : 'rgba(42, 45, 52, 0.08)'),
                        color: d.estadoLogistico === 'ENTREGADO' ? '#009DDC' : d.estadoLogistico === 'EN_TRANSITO' ? '#F26430' : (darkMode ? '#FFFFFF' : '#2A2D34'),
                        border: `1px solid ${d.estadoLogistico === 'ENTREGADO' ? 'rgba(0, 157, 220, 0.25)' : d.estadoLogistico === 'EN_TRANSITO' ? 'rgba(242, 100, 48, 0.2)' : (darkMode ? 'rgba(255, 255, 255, 0.2)' : 'rgba(42, 45, 52, 0.2)')}`
                      }}>
                        {d.estadoLogistico}
                      </span>
                    </td>
                    <td style={styles.td}>
                      {d.estadoLogistico === 'EN_ACOPIO' && (
                        <button style={styles.buttonAccent} onClick={() => cambiarEstadoDonacion(d.id, 'EN_TRANSITO')}>
                          Despachar
                        </button>
                      )}
                      {d.estadoLogistico === 'EN_TRANSITO' && (
                        <button style={styles.buttonPrimary} onClick={() => cambiarEstadoDonacion(d.id, 'ENTREGADO')}>
                          Entregar
                        </button>
                      )}
                      {d.estadoLogistico === 'ENTREGADO' && (
                        <span style={{ fontSize: '0.85rem', color: '#009DDC', fontStyle: 'italic' }}>Finalizado</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p style={{ fontStyle: 'italic', color: '#009DDC', textAlign: 'center', padding: '2rem 0' }}>No hay donaciones registradas en el sistema o servicio no disponible.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminView;
