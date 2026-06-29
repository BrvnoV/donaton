import React, { useState } from 'react';
import Dashboard from './views/Dashboard';
import './App.css';

function App() {
  const [darkMode, setDarkMode] = useState(false);

  return (
    <div style={{ 
      display: 'flex', 
      flexDirection: 'column', 
      minHeight: '100vh',
      backgroundColor: darkMode ? '#1B1D21' : '#F8F9FA',
      color: darkMode ? '#FFFFFF' : '#2A2D34',
      fontFamily: '"Inter", "Segoe UI", Roboto, sans-serif',
      transition: 'all 0.3s ease'
    }}>
      {/* Barra de Navegación Institucional */}
      <nav style={{
        backgroundColor: darkMode ? '#2A2D34' : '#FFFFFF',
        color: darkMode ? '#FFFFFF' : '#2A2D34',
        padding: '1.25rem 5%',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        borderBottom: `1.5px solid ${darkMode ? 'rgba(255, 255, 255, 0.15)' : 'rgba(42, 45, 52, 0.15)'}`,
        boxShadow: darkMode ? '0 4px 20px rgba(0, 0, 0, 0.4)' : '0 4px 20px rgba(42, 45, 52, 0.05)',
        transition: 'all 0.3s ease'
      }}>
        <h1 style={{ margin: 0, fontWeight: 800, fontSize: '1.6rem', color: '#009DDC', letterSpacing: '-0.5px' }}>Plataforma Donaton 🇨🇱</h1>
        <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
          <span style={{
            fontSize: '0.9rem',
            fontWeight: 600,
            color: '#009DDC',
            backgroundColor: darkMode ? 'rgba(0, 157, 220, 0.15)' : 'rgba(0, 157, 220, 0.1)',
            padding: '0.4rem 0.8rem',
            borderRadius: '20px',
            border: '1px solid rgba(0, 157, 220, 0.2)'
          }}>Centro de Operaciones de Emergencia</span>
          <button 
            onClick={() => setDarkMode(!darkMode)}
            style={{
              background: '#009DDC',
              color: '#ffffff',
              border: 'none',
              borderRadius: '20px',
              padding: '0.4rem 1rem',
              cursor: 'pointer',
              fontWeight: '700',
              fontSize: '0.85rem',
              transition: 'all 0.3s ease'
            }}
          >
            {darkMode ? '☀️ Modo Claro' : '🌙 Modo Oscuro'}
          </button>
        </div>
      </nav>

      {/* Panel Central con los Microservicios */}
      <main style={{ flex: 1, transition: 'all 0.3s ease' }}>
        <Dashboard darkMode={darkMode} />
      </main>

      {/* Footer Unificado */}
      <footer style={{
        backgroundColor: '#2A2D34',
        color: '#FFFFFF',
        padding: '2rem 5%',
        textAlign: 'center',
        fontSize: '0.85rem',
        fontWeight: 500,
        borderTop: '4px solid #009DDC',
        marginTop: '5rem',
        transition: 'all 0.3s ease'
      }}>
        <p style={{ opacity: 0.9, margin: 0 }}>© {new Date().getFullYear()} Donaton. Todos los derechos reservados. Centro de Operaciones de Emergencia, Chile 🇨🇱</p>
      </footer>
    </div>
  );
}

export default App;