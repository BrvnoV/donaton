import React, { useState } from 'react';
import StatusAlert from './StatusAlert';

const DonorView = ({
  campanas,
  resumenCampanas,
  formDonacion,
  setFormDonacion,
  manejarDonacionSubmit,
  styles,
  darkMode
}) => {
  // Local state for hovers to achieve high-end premium styling without CSS classes
  const [btnHover, setBtnHover] = useState(false);
  const [cardHovered, setCardHovered] = useState(null);

  // Filter campaigns to show only active ones
  const activeCampanas = Array.isArray(campanas)
    ? campanas.filter(c => c.estado === 'ACTIVE')
    : [];

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '4rem', transition: 'all 0.3s ease' }}>
      
      {/* ================= SECCIÓN 1: HERO SECTION & FORMULARIO INTEGRADO ================= */}
      <div 
        style={{ 
          display: 'grid', 
          gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', 
          gap: '3rem',
          alignItems: 'center',
          padding: '2rem 0'
        }}
      >
        {/* Columna Izquierda: Mensaje de Identidad */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
          <h1 
            style={{ 
              fontSize: '3.5rem', 
              fontWeight: '800', 
              color: darkMode ? '#FFFFFF' : '#009DDC', 
              lineHeight: '1.15', 
              margin: 0, 
              letterSpacing: '-1.5px',
              textTransform: 'uppercase',
              transition: 'all 0.3s ease'
            }}
          >
            Dona para marcar la diferencia.
          </h1>
          <p 
            style={{ 
              fontSize: '1.1rem', 
              lineHeight: '1.8', 
              color: darkMode ? '#B0B7C6' : '#5C6475', 
              margin: 0,
              textAlign: 'justify',
              transition: 'all 0.3s ease'
            }}
          >
            En Donaton, creemos en la solidaridad estructurada y transparente. Nuestra plataforma canaliza tus recursos directamente a comunidades afectadas por crisis operacionales o emergencias humanitarias. Cada aporte cuenta para construir un puente de asistencia rápida, eficiente y medible en terreno.
          </p>
          <div 
            style={{ 
              display: 'inline-flex', 
              alignItems: 'center', 
              gap: '8px', 
              padding: '0.6rem 1.2rem', 
              backgroundColor: darkMode ? '#2A2D34' : '#ffffff', 
              borderRadius: '30px', 
              border: `1.5px dashed ${darkMode ? 'rgba(255,255,255,0.3)' : '#2A2D34'}`, 
              color: darkMode ? '#FFFFFF' : '#2A2D34', 
              fontWeight: '700', 
              fontSize: '0.85rem',
              alignSelf: 'flex-start',
              boxShadow: darkMode ? '0 4px 10px rgba(0,0,0,0.3)' : '0 4px 10px rgba(42,45,52,0.04)',
              transition: 'all 0.3s ease'
            }}
          >
            🛡️ <span>100% de los aportes son validados y auditados en tiempo real</span>
          </div>
        </div>

        {/* Columna Derecha: Caja de Donación Flotante */}
        <div 
          style={{ 
            backgroundColor: darkMode ? '#2A2D34' : '#ffffff', 
            borderRadius: '24px', 
            boxShadow: darkMode ? '0 20px 40px rgba(0,0,0,0.5)' : '0 20px 40px rgba(0,157,220,0.08)', 
            padding: '2.5rem', 
            border: `1.5px solid ${darkMode ? 'rgba(255,255,255,0.1)' : 'rgba(42,45,52,0.15)'}`,
            position: 'relative',
            transition: 'all 0.3s ease'
          }}
        >
          <h3 style={{ ...styles.cardTitle, borderBottom: `2px solid ${darkMode ? 'rgba(255,255,255,0.1)' : '#F8F9FA'}`, paddingBottom: '1rem', marginBottom: '1.5rem' }}>
            Registrar Aporte
          </h3>

          <StatusAlert data={activeCampanas.length > 0 ? activeCampanas : null} defaultMessage="Cargando iniciativas activas..." />

          <form onSubmit={manejarDonacionSubmit}>
            <div style={styles.formGroup}>
              <label style={styles.label}>Seleccionar Campaña Destinataria *</label>
              <select
                style={styles.input}
                required
                value={formDonacion.campanaId}
                onChange={e => setFormDonacion({ ...formDonacion, campanaId: e.target.value })}
              >
                <option value="">-- Seleccionar campaña activa --</option>
                {activeCampanas.map(c => (
                  <option key={c.id} value={c.id}>{c.nombre}</option>
                ))}
              </select>
            </div>

            <div style={styles.formGroup}>
              <label style={styles.label}>Nombre del Donante</label>
              <input
                type="text"
                style={styles.input}
                placeholder="Ej: Anónimo, Empresa X"
                value={formDonacion.donante}
                onChange={e => setFormDonacion({ ...formDonacion, donante: e.target.value })}
              />
            </div>

            <div style={styles.formGroup}>
              <label style={styles.label}>Tipo de Recurso *</label>
              <select
                style={styles.input}
                required
                value={formDonacion.tipoRecurso}
                onChange={e => setFormDonacion({ ...formDonacion, tipoRecurso: e.target.value })}
              >
                <option value="">-- Seleccionar tipo --</option>
                <option value="ALIMENTOS">Alimentos no perecibles</option>
                <option value="ROPA">Ropa / Abrigo</option>
                <option value="DINERO">Aporte Monetario</option>
                <option value="MEDICAMENTOS">Insumos Médicos</option>
                <option value="OTROS">Otros Recursos</option>
              </select>
            </div>

            <div style={styles.formGroup}>
              <label style={styles.label}>Cantidad *</label>
              <input
                type="number"
                style={styles.input}
                min="1"
                required
                value={formDonacion.cantidad}
                onChange={e => setFormDonacion({ ...formDonacion, cantidad: parseFloat(e.target.value) })}
              />
            </div>

            <button 
              type="submit" 
              style={{ 
                ...styles.buttonPrimary, 
                marginTop: '1.25rem', 
                backgroundColor: btnHover ? '#007bb5' : '#009DDC',
                transform: btnHover ? 'translateY(-2px)' : 'none',
                transition: 'all 0.2s cubic-bezier(0.4, 0, 0.2, 1)'
              }}
              onMouseEnter={() => setBtnHover(true)}
              onMouseLeave={() => setBtnHover(false)}
            >
              Enviar Donación Solidaria
            </button>
          </form>
        </div>
      </div>

      {/* ================= SECCIÓN 2: BLOQUE INSTRUCTIVO "¿CÓMO PUEDES AYUDAR?" ================= */}
      <div style={{ padding: '4rem 0', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '3rem' }}>
        <div style={{ textAlign: 'center', position: 'relative' }}>
          <h2 style={{ fontSize: '2rem', fontWeight: '800', color: '#009DDC', margin: '0 0 0.75rem 0', transition: 'all 0.3s ease' }}>
            ¿Cómo puedes ayudar?
          </h2>
          <div style={{ width: '60px', height: '4px', backgroundColor: '#F26430', borderRadius: '2px', margin: '0 auto', transition: 'all 0.3s ease' }}></div>
        </div>

        <div 
          style={{ 
            display: 'grid', 
            gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', 
            gap: '2.5rem',
            width: '100%'
          }}
        >
          {/* Paso 1 */}
          <div 
            style={{ 
              backgroundColor: darkMode ? '#2A2D34' : '#ffffff', 
              border: `1.5px solid ${darkMode ? 'rgba(255,255,255,0.1)' : 'rgba(42,45,52,0.15)'}`, 
              padding: '2.5rem 2rem', 
              borderRadius: '20px', 
              textAlign: 'center', 
              display: 'flex', 
              flexDirection: 'column', 
              alignItems: 'center', 
              gap: '1rem',
              boxShadow: darkMode ? '0 10px 30px rgba(0,0,0,0.3)' : '0 10px 30px rgba(0,157,220,0.05)',
              transition: 'all 0.3s ease'
            }}
          >
            <div 
              style={{ 
                width: '60px', 
                height: '60px', 
                borderRadius: '50%', 
                backgroundColor: darkMode ? 'rgba(0,157,220,0.2)' : 'rgba(0,157,220,0.1)', 
                color: '#009DDC', 
                display: 'flex', 
                alignItems: 'center', 
                justifyContent: 'center', 
                fontSize: '1.5rem', 
                fontWeight: '800',
                transition: 'all 0.3s ease'
              }}
            >
              1
            </div>
            <h4 style={{ fontSize: '1.2rem', fontWeight: '700', color: '#009DDC', margin: 0, transition: 'all 0.3s ease' }}>Descubre</h4>
            <p style={{ fontSize: '0.95rem', lineHeight: '1.6', color: darkMode ? '#B0B7C6' : '#5C6475', margin: 0, transition: 'all 0.3s ease' }}>
              Explora las diferentes causas, emergencias y programas activos de asistencia comunitaria disponibles.
            </p>
          </div>

          {/* Paso 2 */}
          <div 
            style={{ 
              backgroundColor: darkMode ? '#2A2D34' : '#ffffff', 
              border: `1.5px solid ${darkMode ? 'rgba(255,255,255,0.1)' : 'rgba(42,45,52,0.15)'}`, 
              padding: '2.5rem 2rem', 
              borderRadius: '20px', 
              textAlign: 'center', 
              display: 'flex', 
              flexDirection: 'column', 
              alignItems: 'center', 
              gap: '1rem',
              boxShadow: darkMode ? '0 10px 30px rgba(0,0,0,0.3)' : '0 10px 30px rgba(0,157,220,0.05)',
              transition: 'all 0.3s ease'
            }}
          >
            <div 
              style={{ 
                width: '60px', 
                height: '60px', 
                borderRadius: '50%', 
                backgroundColor: darkMode ? 'rgba(0,157,220,0.2)' : 'rgba(0,157,220,0.1)', 
                color: '#009DDC', 
                display: 'flex', 
                alignItems: 'center', 
                justifyContent: 'center', 
                fontSize: '1.5rem', 
                fontWeight: '800',
                transition: 'all 0.3s ease'
              }}
            >
              2
            </div>
            <h4 style={{ fontSize: '1.2rem', fontWeight: '700', color: '#009DDC', margin: 0, transition: 'all 0.3s ease' }}>Elige</h4>
            <p style={{ fontSize: '0.95rem', lineHeight: '1.6', color: darkMode ? '#B0B7C6' : '#5C6475', margin: 0, transition: 'all 0.3s ease' }}>
              Selecciona el proyecto activo que desees apoyar en el catálogo y conoce sus necesidades de recursos.
            </p>
          </div>

          {/* Paso 3 */}
          <div 
            style={{ 
              backgroundColor: darkMode ? '#2A2D34' : '#ffffff', 
              border: `1.5px solid ${darkMode ? 'rgba(255,255,255,0.1)' : 'rgba(42,45,52,0.15)'}`, 
              padding: '2.5rem 2rem', 
              borderRadius: '20px', 
              textAlign: 'center', 
              display: 'flex', 
              flexDirection: 'column', 
              alignItems: 'center', 
              gap: '1rem',
              boxShadow: darkMode ? '0 10px 30px rgba(0,0,0,0.3)' : '0 10px 30px rgba(0,157,220,0.05)',
              transition: 'all 0.3s ease'
            }}
          >
            <div 
              style={{ 
                width: '60px', 
                height: '60px', 
                borderRadius: '50%', 
                backgroundColor: darkMode ? 'rgba(0,157,220,0.2)' : 'rgba(0,157,220,0.1)', 
                color: '#009DDC', 
                display: 'flex', 
                alignItems: 'center', 
                justifyContent: 'center', 
                fontSize: '1.5rem', 
                fontWeight: '800',
                transition: 'all 0.3s ease'
              }}
            >
              3
            </div>
            <h4 style={{ fontSize: '1.2rem', fontWeight: '700', color: '#009DDC', margin: 0, transition: 'all 0.3s ease' }}>Dona</h4>
            <p style={{ fontSize: '0.95rem', lineHeight: '1.6', color: darkMode ? '#B0B7C6' : '#5C6475', margin: 0, transition: 'all 0.3s ease' }}>
              Registra tu aporte. La transacción y la logística de despacho se monitorizan de manera pública.
            </p>
          </div>
        </div>
      </div>

      {/* ================= SECCIÓN 3: CATÁLOGO DE PROYECTOS ACTIVOS ================= */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem', paddingBottom: '3rem' }}>
        <div style={{ textAlign: 'center' }}>
          <span style={{ fontSize: '0.85rem', fontWeight: '800', color: '#F26430', textTransform: 'uppercase', letterSpacing: '1px', transition: 'all 0.3s ease' }}>
            Tenemos Muchas Causas Que Necesitan Ayuda
          </span>
          <h2 style={{ fontSize: '2.25rem', fontWeight: '800', color: '#009DDC', margin: '0.5rem 0 0 0', transition: 'all 0.3s ease' }}>
            Proyectos Activos
          </h2>
        </div>

        <StatusAlert data={resumenCampanas.length > 0 ? resumenCampanas : null} defaultMessage="Cargando catálogo de proyectos..." />

        <div 
          style={{ 
            display: 'grid', 
            gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', 
            gap: '2.5rem' 
          }}
        >
          {Array.isArray(campanas) && campanas.filter(c => c.estado === 'ACTIVE').map(c => {
            const isHovered = cardHovered === c.id;
            // Match campaign with summary to fetch aggregated resources count
            const resumen = Array.isArray(resumenCampanas) 
              ? resumenCampanas.find(rc => rc.id === c.id) 
              : null;
            const totalRecursos = resumen ? resumen.totalRecursos : 0;

            return (
              <div 
                key={c.id}
                style={{ 
                  backgroundColor: darkMode ? '#2A2D34' : '#ffffff', 
                  borderRadius: '20px', 
                  border: `1.5px solid ${darkMode ? 'rgba(255,255,255,0.15)' : 'rgba(42,45,52,0.15)'}`,
                  boxShadow: isHovered ? (darkMode ? '0 15px 35px rgba(0,0,0,0.6)' : '0 15px 35px rgba(0,157,220,0.15)') : (darkMode ? '0 6px 18px rgba(0,0,0,0.3)' : '0 6px 18px rgba(0,157,220,0.05)'),
                  transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                  transform: isHovered ? 'translateY(-6px)' : 'none',
                  display: 'flex',
                  flexDirection: 'column',
                  height: '100%',
                  overflow: 'hidden'
                }}
                onMouseEnter={() => setCardHovered(c.id)}
                onMouseLeave={() => setCardHovered(null)}
              >
                {/* Simulated Image Header with Soft Gradient */}
                <div 
                  style={{ 
                    height: '180px', 
                    background: 'linear-gradient(135deg, #009DDC 0%, #2A2D34 100%)', 
                    position: 'relative',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                  }}
                >
                  <span style={{ fontSize: '3.5rem' }}>🌍</span>
                  <div 
                    style={{ 
                      position: 'absolute', 
                      top: '15px', 
                      left: '15px', 
                      backgroundColor: '#F26430', 
                      color: '#ffffff', 
                      padding: '0.35rem 0.85rem', 
                      borderRadius: '20px', 
                      fontWeight: '700', 
                      fontSize: '0.75rem',
                      boxShadow: '0 2px 8px rgba(0,0,0,0.2)'
                    }}
                  >
                    ORGANIZACIÓN
                  </div>
                </div>

                {/* Card Body */}
                <div style={{ padding: '1.75rem', display: 'flex', flexDirection: 'column', gap: '1rem', flexGrow: 1 }}>
                  <h4 style={{ fontSize: '1.25rem', fontWeight: '800', color: '#009DDC', margin: 0, transition: 'all 0.3s ease' }}>
                    {c.nombre}
                  </h4>
                  <p style={{ fontSize: '0.95rem', lineHeight: '1.6', color: darkMode ? '#FFFFFF' : '#2A2D34', margin: 0, flexGrow: 1, transition: 'all 0.3s ease' }}>
                    {c.descripcion}
                  </p>

                  {/* Metrics Section */}
                  <div 
                    style={{ 
                      backgroundColor: darkMode ? '#1B1D21' : '#F8F9FA', 
                      padding: '1rem', 
                      borderRadius: '12px', 
                      border: `1px solid ${darkMode ? 'rgba(255,255,255,0.1)' : 'rgba(42,45,52,0.1)'}`,
                      display: 'flex',
                      flexDirection: 'column',
                      gap: '0.5rem',
                      marginTop: '0.5rem',
                      transition: 'all 0.3s ease'
                    }}
                  >
                    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', fontWeight: '700', color: darkMode ? '#B0B7C6' : '#5C6475', transition: 'all 0.3s ease' }}>
                      <span>Ayuda Recibida</span>
                      <span style={{ color: '#009DDC' }}>{totalRecursos} recursos</span>
                    </div>
                    {/* Simulated elegant progress bar indicator */}
                    <div style={{ width: '100%', height: '8px', backgroundColor: darkMode ? '#2A2D34' : '#ffffff', borderRadius: '4px', overflow: 'hidden', border: `1px solid ${darkMode ? 'rgba(255,255,255,0.1)' : 'rgba(42,45,52,0.1)'}` }}>
                      <div 
                        style={{ 
                          width: `${Math.min(100, (totalRecursos / 10) * 100)}%`, 
                          height: '100%', 
                          backgroundColor: '#009DDC',
                          borderRadius: '4px' 
                        }}
                      ></div>
                    </div>
                  </div>

                  {/* Action Button */}
                  <button 
                    style={{ 
                      marginTop: '1rem', 
                      padding: '0.8rem 1.5rem', 
                      borderRadius: '30px', 
                      border: '2px solid #009DDC', 
                      color: isHovered ? '#ffffff' : '#009DDC', 
                      backgroundColor: isHovered ? '#009DDC' : 'transparent', 
                      fontWeight: '700', 
                      textTransform: 'uppercase', 
                      fontSize: '0.8rem',
                      cursor: 'pointer',
                      transition: 'all 0.25s ease',
                      width: '100%',
                      boxShadow: isHovered ? (darkMode ? '0 4px 12px rgba(0,157,220,0.5)' : '0 4px 12px rgba(0,157,220,0.3)') : 'none'
                    }}
                    onClick={() => {
                      setFormDonacion({ ...formDonacion, campanaId: c.id.toString() });
                      window.scrollTo({ top: 0, behavior: 'smooth' });
                    }}
                  >
                    Apoyar Esta Causa
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default DonorView;
