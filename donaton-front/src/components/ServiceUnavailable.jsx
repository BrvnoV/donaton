import React from 'react';

const ServiceUnavailable = ({ darkMode }) => {
  return (
    <div style={{
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      height: '50vh',
      textAlign: 'center',
      padding: '2rem',
      backgroundColor: darkMode ? '#1B1D21' : '#F8F9FA',
      color: darkMode ? '#FFFFFF' : '#2A2D34'
    }}>
      <div style={{ fontSize: '4rem', marginBottom: '1rem' }}>⚠️</div>
      <h2 style={{ color: '#F26430', marginBottom: '1rem' }}>Servicio No Disponible</h2>
      <p style={{ maxWidth: '400px', lineHeight: '1.6' }}>
        Servicio temporalmente fuera de servicio por alta demanda o mantenimiento de red. Intente nuevamente en unos instantes.
      </p>
      <button 
        onClick={() => window.location.reload()}
        style={{
          marginTop: '2rem',
          padding: '0.75rem 1.5rem',
          backgroundColor: '#009DDC',
          color: '#FFF',
          border: 'none',
          borderRadius: '25px',
          cursor: 'pointer',
          fontWeight: 'bold',
          transition: 'all 0.3s ease'
        }}
      >
        Reintentar Conexión
      </button>
    </div>
  );
};

export default ServiceUnavailable;
