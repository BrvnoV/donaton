import React from 'react';
import StatusAlert from './StatusAlert';

const VolunteerView = ({
  campanas,
  formVoluntario,
  setFormVoluntario,
  manejarVoluntarioSubmit,
  styles,
  darkMode
}) => {
  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', ...styles.card, transition: 'all 0.3s ease' }}>
      <h3 style={styles.cardTitle}>Inscripción de Voluntarios</h3>
      <p style={{ fontSize: '0.95rem', color: darkMode ? '#FFFFFF' : '#2A2D34', marginBottom: '2rem', lineHeight: '1.5', transition: 'all 0.3s ease' }}>
        Regístrate y apoya activamente en terreno a las campañas de ayuda humanitaria vigentes.
      </p>

      <StatusAlert data={campanas.length > 0 ? campanas : null} defaultMessage="No se pudieron cargar las campañas activas. El servicio de campañas podría estar fuera de línea." />

      <form onSubmit={manejarVoluntarioSubmit}>
        <div style={styles.formGroup}>
          <label style={styles.label}>Selecciona la Campaña *</label>
          <select
            style={styles.input}
            required
            value={formVoluntario.campanaId}
            onChange={e => setFormVoluntario({ ...formVoluntario, campanaId: e.target.value })}
          >
            <option value="">-- Seleccionar campaña --</option>
            {Array.isArray(campanas) && campanas.filter(c => c.estado === 'ACTIVE').map(c => (
              <option key={c.id} value={c.id}>{c.nombre} [{c.estado}]</option>
            ))}
          </select>
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Nombre Completo *</label>
          <input
            type="text"
            style={styles.input}
            required
            placeholder="Nombre y Apellido"
            value={formVoluntario.nombre}
            onChange={e => setFormVoluntario({ ...formVoluntario, nombre: e.target.value })}
          />
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Contacto Telefónico *</label>
          <input
            type="text"
            style={styles.input}
            required
            placeholder="Ej: +56912345678"
            value={formVoluntario.contacto}
            onChange={e => setFormVoluntario({ ...formVoluntario, contacto: e.target.value })}
          />
        </div>

        <div style={styles.formGroup}>
          <label style={styles.label}>Correo Electrónico *</label>
          <input
            type="email"
            style={styles.input}
            required
            placeholder="ejemplo@correo.com"
            value={formVoluntario.correo}
            onChange={e => setFormVoluntario({ ...formVoluntario, correo: e.target.value })}
          />
        </div>

        <button type="submit" style={{ ...styles.buttonPrimary, marginTop: '1rem' }}>
          Registrar Inscripción
        </button>
      </form>
    </div>
  );
};

export default VolunteerView;
