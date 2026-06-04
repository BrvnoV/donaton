import React from 'react';
import Dashboard from './views/Dashboard';
import './App.css';

function App() {
  return (
    <div>
      {/* Barra de Navegación Institucional */}
      <nav className="navbar">
        <h1 style={{ margin: 0, fontSize: '1.5rem' }}>Plataforma Donaton 🇨🇱</h1>
        <span style={{ opacity: 0.8, fontSize: '0.9rem', fontWeight: '500' }}>
          Centro de Operaciones de Emergencia
        </span>
      </nav>

      {/* Panel Central con los Microservicios */}
      <Dashboard />
    </div>
  );
}

export default App;