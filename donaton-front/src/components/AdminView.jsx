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
  manejarDonacionDelete,
  editandoCampanaId,
  setEditandoCampanaId,
  formEditCampana,
  setFormEditCampana,
  styles,
  onLogout,
  darkMode,
  voluntariosMap,
  despacharCampana,
  entregarCampana
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
            Crear
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
                    <td style={styles.td}>{idx + 1}</td>
                    
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
                            {c.estado === 'ACTIVE' && (() => {
                              const tieneDonaciones = donaciones.some(d => d.campanaId === c.id);
                              return (
                                <button
                                  style={{ ...styles.buttonAction, opacity: tieneDonaciones ? 1 : 0.45, cursor: tieneDonaciones ? 'pointer' : 'not-allowed' }}
                                  onClick={() => cambiarEstadoCampana(c.id, 'COMPLETED')}
                                  title={!tieneDonaciones ? 'Se requiere al menos una donación para completar la campaña' : ''}
                                >
                                  Completar
                                </button>
                              );
                            })()}
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
        
        {/* Campañas con sus voluntarios */}
        <h4 style={{ ...styles.cardTitle, fontSize: '1.05rem', marginTop: '1rem', borderBottom: 'none', marginBottom: '1rem' }}>Campañas y Voluntarios Inscritos</h4>
        {campanas.length > 0 ? (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginBottom: '2.5rem' }}>
            {campanas.map(c => {
              const vols = voluntariosMap[c.id] || [];
              const estadoColor = c.estado === 'ACTIVE' ? '#009DDC' : c.estado === 'COMPLETED' ? '#4CAF50' : c.estado === 'PLANNED' ? '#F26430' : '#9E9E9E';
              const pendientesDespacho = c.estado === 'COMPLETED' && donaciones.some(d => d.campanaId === c.id && d.estadoLogistico === 'EN_ACOPIO');
              const pendientesEntrega = c.estado === 'COMPLETED' && donaciones.some(d => d.campanaId === c.id && d.estadoLogistico === 'EN_TRANSITO');

              return (
                <div key={c.id} style={{ padding: '1.25rem', border: `1px solid ${darkMode ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.08)'}`, borderRadius: '10px', backgroundColor: darkMode ? 'rgba(255,255,255,0.02)' : '#FAFAFA' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '1rem' }}>
                    <div style={{ flex: '1 1 280px' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.5rem', flexWrap: 'wrap' }}>
                        <h5 style={{ margin: 0, color: darkMode ? '#FFFFFF' : '#2A2D34', fontSize: '1rem' }}>#{c.id} — {c.nombre}</h5>
                        <span style={{ padding: '0.2rem 0.6rem', borderRadius: '20px', fontSize: '0.75rem', fontWeight: '700', border: `1px solid ${estadoColor}`, color: estadoColor }}>{c.estado}</span>
                      </div>
                      <p style={{ margin: '0 0 0.75rem 0', fontSize: '0.85rem', color: darkMode ? '#B0B7C6' : '#5C6475' }}>{c.descripcion}</p>
                      
                      <div style={{ padding: '0.75rem 1rem', backgroundColor: darkMode ? 'rgba(0,0,0,0.25)' : '#FFFFFF', borderRadius: '8px', border: `1px solid ${darkMode ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.06)'}` }}>
                        <p style={{ margin: '0 0 0.5rem 0', fontSize: '0.85rem', fontWeight: '700', color: '#009DDC' }}>
                          👥 Voluntarios inscritos: {vols.length}
                        </p>
                        {vols.length > 0 ? (
                          <ul style={{ margin: 0, paddingLeft: '1.2rem', fontSize: '0.85rem', color: darkMode ? '#E2E8F0' : '#4A5568', display: 'flex', flexDirection: 'column', gap: '0.2rem' }}>
                            {vols.map(v => (
                              <li key={v.id}>
                                <strong>{v.nombre}</strong> <span style={{ opacity: 0.75 }}>— {v.correo} — 📞 {v.contacto}</span>
                              </li>
                            ))}
                          </ul>
                        ) : (
                          <p style={{ margin: 0, fontSize: '0.85rem', fontStyle: 'italic', color: '#9E9E9E' }}>Sin voluntarios inscritos aún.</p>
                        )}
                      </div>
                    </div>

                    {c.estado === 'COMPLETED' && (
                      <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem', minWidth: '155px' }}>
                        <button
                          style={{ ...styles.buttonAccent, opacity: (!pendientesDespacho || vols.length === 0) ? 0.45 : 1, fontSize: '0.85rem', padding: '0.6rem 1rem' }}
                          onClick={() => despacharCampana(c.id)}
                          disabled={!pendientesDespacho || vols.length === 0}
                          title={vols.length === 0 ? 'Se requieren voluntarios para despachar' : !pendientesDespacho ? 'No hay donaciones en acopio' : ''}
                        >
                          📦 Despachar Todo
                        </button>
                        <button
                          style={{ ...styles.buttonPrimary, opacity: (!pendientesEntrega || vols.length === 0) ? 0.45 : 1, fontSize: '0.85rem', padding: '0.6rem 1rem' }}
                          onClick={() => entregarCampana(c.id)}
                          disabled={!pendientesEntrega || vols.length === 0}
                          title={!pendientesEntrega ? 'No hay donaciones en tránsito' : ''}
                        >
                          ✅ Entregar Todo
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        ) : (
          <p style={{ fontSize: '0.9rem', color: darkMode ? '#B0B7C6' : '#5C6475', marginBottom: '2rem' }}>No hay campañas registradas.</p>
        )}

        {/* Tabla de donaciones */}
        <h4 style={{ ...styles.cardTitle, fontSize: '1.05rem', marginTop: '1rem', borderBottom: 'none', marginBottom: '1rem' }}>Listado de Donaciones</h4>
        <StatusAlert data={donaciones.length > 0 ? donaciones : null} defaultMessage="No se pudieron cargar las donaciones. El servicio de donaciones podría estar fuera de línea." />

        <div style={{ overflowX: 'auto' }}>
          {Array.isArray(donaciones) && donaciones.length > 0 ? (
            <table style={styles.table}>
              <thead>
                <tr>
                  <th style={styles.th}>#</th>
                  <th style={styles.th}>Campaña</th>
                  <th style={styles.th}>Donante</th>
                  <th style={styles.th}>Recurso</th>
                  <th style={styles.th}>Cantidad</th>
                  <th style={styles.th}>Estado Logístico</th>
                  <th style={styles.th}>Gestión</th>
                </tr>
              </thead>
              <tbody>
                {donaciones.map((d, idx) => {
                  const campana = campanas.find(c => c.id === d.campanaId);
                  return (
                    <tr key={d.id} style={{ backgroundColor: darkMode && idx % 2 === 0 ? 'rgba(255,255,255,0.02)' : 'transparent', transition: 'all 0.3s ease' }}>
                      <td style={styles.td}>{idx + 1}</td>
                      <td style={{ ...styles.td, fontWeight: '700', color: darkMode ? '#FFFFFF' : '#2A2D34' }}>
                        {campana ? campana.nombre : `#${d.campanaId}`}
                      </td>
                      <td style={styles.td}>{d.donante || 'Anónimo'}</td>
                      <td style={styles.td}>{d.tipoRecurso}</td>
                      <td style={{ ...styles.td, fontWeight: '700', color: '#009DDC' }}>{d.cantidad}</td>
                      <td style={styles.td}>
                        <span style={{
                          padding: '0.35rem 0.75rem',
                          borderRadius: '20px',
                          fontSize: '0.8rem',
                          fontWeight: '700',
                          backgroundColor: d.estadoLogistico === 'ENTREGADO' ? 'rgba(76, 175, 80, 0.12)' : d.estadoLogistico === 'EN_TRANSITO' ? 'rgba(242, 100, 48, 0.1)' : (darkMode ? 'rgba(255, 255, 255, 0.08)' : 'rgba(42, 45, 52, 0.08)'),
                          color: d.estadoLogistico === 'ENTREGADO' ? '#4CAF50' : d.estadoLogistico === 'EN_TRANSITO' ? '#F26430' : (darkMode ? '#FFFFFF' : '#2A2D34'),
                          border: `1px solid ${d.estadoLogistico === 'ENTREGADO' ? 'rgba(76, 175, 80, 0.3)' : d.estadoLogistico === 'EN_TRANSITO' ? 'rgba(242, 100, 48, 0.2)' : (darkMode ? 'rgba(255, 255, 255, 0.2)' : 'rgba(42, 45, 52, 0.2)')}`
                        }}>
                          {d.estadoLogistico === 'EN_ACOPIO' ? '📦 En Acopio' : d.estadoLogistico === 'EN_TRANSITO' ? '🚚 En Tránsito' : '✅ Entregado'}
                        </span>
                      </td>
                      <td style={styles.td}>
                        <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                          <button style={{ ...styles.buttonAction, backgroundColor: darkMode ? 'rgba(242, 100, 48, 0.15)' : 'rgba(242, 100, 48, 0.05)', border: '1.5px solid #F26430', color: '#F26430', padding: '0.3rem 0.8rem', fontSize: '0.8rem' }} onClick={() => manejarDonacionDelete && manejarDonacionDelete(d.id)}>
                            🗑️ Eliminar
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
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
