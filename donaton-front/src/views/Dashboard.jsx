import React, { useState, useEffect } from 'react';
import DonorLanding from '../components/DonorLanding';
import VolunteerView from '../components/VolunteerView';
import AdminView from '../components/AdminView';
import clienteAxios, { interceptoresAxios } from '../api/clienteAxios';
import ServiceUnavailable from '../components/ServiceUnavailable';

const Dashboard = ({ darkMode }) => {
  const [activeTab, setActiveTab] = useState('donante');
  const [isLoading, setIsLoading] = useState(false);
  const [isServiceUnavailable, setServiceUnavailable] = useState(false);
  const [campanas, setCampanas] = useState([]);
  const [donaciones, setDonaciones] = useState([]);
  const [resumenCampanas, setResumenCampanas] = useState([]);

  // JWT Token state
  const [token, setToken] = useState(sessionStorage.getItem('token'));
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const [alertInfo, setAlertInfo] = useState({ show: false, type: '', message: '' });

  const [formDonacion, setFormDonacion] = useState({ campanaId: '', donante: '', tipoRecurso: '', cantidad: 1 });
  const [formVoluntario, setFormVoluntario] = useState({ nombre: '', contacto: '', correo: '', campanaId: '' });
  const [formCampana, setFormCampana] = useState({ nombre: '', descripcion: '' });

  const [editandoCampanaId, setEditandoCampanaId] = useState(null);
  const [formEditCampana, setFormEditCampana] = useState({ nombre: '', descripcion: '' });

  const [voluntariosMap, setVoluntariosMap] = useState({});

  useEffect(() => {
    interceptoresAxios(setIsLoading, setServiceUnavailable);
  }, []);

  const cargarDatos = () => {
    clienteAxios.get('/campanas')
      .then(res => {
        const camps = Array.isArray(res.data) ? res.data : [];
        setCampanas(camps);
        
        const currentToken = sessionStorage.getItem('token');
        if (currentToken) {
          camps.forEach(c => {
            clienteAxios.get(`/campanas/${c.id}/voluntarios`, {
              headers: { 'Authorization': `Bearer ${currentToken}` }
            })
            .then(vRes => {
              // BFF returns {campana, voluntarios} object
              let vols = [];
              if (Array.isArray(vRes.data)) {
                vols = vRes.data;
              } else if (vRes.data && Array.isArray(vRes.data.voluntarios)) {
                vols = vRes.data.voluntarios;
              }
              setVoluntariosMap(prev => ({ ...prev, [c.id]: vols }));
            })
            .catch(e => console.error(e));
          });
        }
      })
      .catch(() => setCampanas([]));

    clienteAxios.get('/campanas/resumen')
      .then(res => setResumenCampanas(Array.isArray(res.data) ? res.data : []))
      .catch(() => setResumenCampanas([]));

    const currentToken = sessionStorage.getItem('token');
    if (currentToken) {
      clienteAxios.get('/donaciones', {
        headers: { 'Authorization': `Bearer ${currentToken}` }
      })
        .then(res => setDonaciones(Array.isArray(res.data) ? res.data : []))
        .catch((err) => {
          if (err.response && (err.response.status === 401 || err.response.status === 403)) {
            sessionStorage.removeItem('token');
            setToken(null);
          }
          console.error(err);
          setDonaciones([]);
        });
    } else {
      setDonaciones([]);
    }
  };

  useEffect(() => {
    cargarDatos();
  }, [token]);

  const mostrarAlerta = (type, message) => {
    setAlertInfo({ show: true, type, message });
    setTimeout(() => {
      setAlertInfo({ show: false, type: '', message: '' });
    }, 5000);
  };

  const manejarLoginSubmit = (e) => {
    e.preventDefault();
    clienteAxios.post('/auth/login', { username, password })
      .then(res => {
        sessionStorage.setItem('token', res.data.token);
        setToken(res.data.token);
        setUsername('');
        setPassword('');
        mostrarAlerta('success', 'Sesión iniciada correctamente');
      })
      .catch(err => {
        mostrarAlerta('error', err.response?.data?.message || 'Credenciales incorrectas o servidor fuera de línea');
      });
  };

  const manejarLogout = () => {
    sessionStorage.removeItem('token');
    setToken(null);
    mostrarAlerta('success', 'Sesión cerrada correctamente');
  };

  const manejarDonacionSubmit = (e) => {
    e.preventDefault();
    if (!formDonacion.campanaId) {
      mostrarAlerta('error', 'Por favor, selecciona una campaña activa para tu donación.');
      return;
    }
    const payload = {
      campanaId: parseInt(formDonacion.campanaId),
      donante: formDonacion.donante,
      tipoRecurso: formDonacion.tipoRecurso,
      cantidad: parseFloat(formDonacion.cantidad)
    };
    clienteAxios.post('/donaciones', payload)
      .then(res => {
        mostrarAlerta('success', '¡Gracias! Tu donación ha sido registrada con éxito.');
        setFormDonacion({ campanaId: '', donante: '', tipoRecurso: '', cantidad: 1 });
        cargarDatos();
      })
      .catch(err => {
        mostrarAlerta('error', err.response?.data?.error || err.response?.data?.message || err.message);
      });
  };

  const manejarVoluntarioSubmit = (e) => {
    e.preventDefault();
    if (!formVoluntario.campanaId) {
      mostrarAlerta('error', 'Por favor, selecciona una campaña para unirte.');
      return;
    }
    const payload = {
      nombre: formVoluntario.nombre,
      contacto: formVoluntario.contacto,
      correo: formVoluntario.correo,
      campanaId: parseInt(formVoluntario.campanaId)
    };
    clienteAxios.post('/voluntarios', payload)
      .then(res => {
        // Check if it's actually an error response returned as 400
        if (res.data && res.data.message && res.status === 400) {
          mostrarAlerta('error', res.data.message);
          return;
        }
        mostrarAlerta('success', '¡Excelente! Te has registrado como voluntario de la campaña.');
        setFormVoluntario({ nombre: '', contacto: '', correo: '', campanaId: '' });
        cargarDatos();
      })
      .catch(err => {
        const msg = err.response?.data?.message || err.response?.data?.error || err.message;
        mostrarAlerta('error', msg || 'No se pudo completar el registro.');
      });
  };

  const manejarCampanaSubmit = (e) => {
    e.preventDefault();
    clienteAxios.post('/campanas', formCampana, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(res => {
        mostrarAlerta('success', 'Campaña registrada correctamente');
        setFormCampana({ nombre: '', descripcion: '' });
        cargarDatos();
      })
      .catch(err => {
        mostrarAlerta('error', err.response?.data?.message || 'Error al registrar la campaña.');
      });
  };

  const cambiarEstadoDonacion = (id, nuevoEstado) => {
    clienteAxios.put(`/donaciones/${id}/estado`, { estadoLogistico: nuevoEstado }, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(res => {
        mostrarAlerta('success', 'Estado de la donación actualizado');
        cargarDatos();
      })
      .catch(err => {
        mostrarAlerta('error', err.response?.data?.message || err.message);
      });
  };

  const cambiarEstadoCampana = (id, nuevoEstado) => {
    // Regla de negocio: no se puede completar sin donaciones
    if (nuevoEstado === 'COMPLETED') {
      const donacionesCampana = donaciones.filter(d => d.campanaId === id);
      if (donacionesCampana.length === 0) {
        mostrarAlerta('error', 'No se puede completar la campaña: debe tener al menos una donación registrada.');
        return;
      }
    }
    clienteAxios.put(`/campanas/${id}/estado`, { estado: nuevoEstado }, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(res => {
        mostrarAlerta('success', 'Estado de la campaña actualizado');
        cargarDatos();
      })
      .catch(err => {
        mostrarAlerta('error', err.response?.data?.message || err.message);
      });
  };

  const manejarCampanaDelete = (id) => {
    if (!window.confirm('¿Estás seguro de eliminar esta campaña?')) return;
    clienteAxios.delete(`/campanas/${id}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(res => {
        mostrarAlerta('success', 'Campaña eliminada correctamente');
        cargarDatos();
      })
      .catch(err => {
        mostrarAlerta('error', err.response?.data?.message || err.message);
      });
  };

  const manejarCampanaUpdate = (e) => {
    e.preventDefault();
    clienteAxios.put(`/campanas/${editandoCampanaId}`, formEditCampana, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(res => {
        mostrarAlerta('success', 'Campaña actualizada correctamente');
        setEditandoCampanaId(null);
        setFormEditCampana({ nombre: '', descripcion: '' });
        cargarDatos();
      })
      .catch(err => {
        mostrarAlerta('error', err.response?.data?.message || err.message);
      });
  };

  const manejarDonacionDelete = (id) => {
    if (!window.confirm('¿Estás seguro de eliminar esta donación?')) return;
    clienteAxios.delete(`/donaciones/${id}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
      .then(res => {
        mostrarAlerta('success', 'Donación eliminada correctamente');
        cargarDatos();
      })
      .catch(err => {
        mostrarAlerta('error', err.response?.data?.message || err.message);
      });
  };

  const despacharCampana = (campanaId) => {
    const donacionesCampana = donaciones.filter(d => d.campanaId === campanaId && d.estadoLogistico === 'EN_ACOPIO');
    if (donacionesCampana.length === 0) {
      mostrarAlerta('error', 'No hay donaciones en acopio para despachar en esta campaña.');
      return;
    }
    Promise.all(donacionesCampana.map(d => 
      clienteAxios.put(`/donaciones/${d.id}/estado`, { estadoLogistico: 'EN_TRANSITO' }, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
    ))
    .then(() => {
      mostrarAlerta('success', 'Todas las donaciones en acopio de la campaña han sido despachadas.');
      cargarDatos();
    })
    .catch(err => {
      mostrarAlerta('error', 'Error al despachar algunas donaciones.');
      cargarDatos();
    });
  };

  const entregarCampana = (campanaId) => {
    const donacionesCampana = donaciones.filter(d => d.campanaId === campanaId && d.estadoLogistico === 'EN_TRANSITO');
    if (donacionesCampana.length === 0) {
      mostrarAlerta('error', 'No hay donaciones en tránsito para entregar en esta campaña.');
      return;
    }
    Promise.all(donacionesCampana.map(d => 
      clienteAxios.put(`/donaciones/${d.id}/estado`, { estadoLogistico: 'ENTREGADO' }, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
    ))
    .then(() => {
      mostrarAlerta('success', 'Todas las donaciones en tránsito de la campaña han sido entregadas.');
      cargarDatos();
    })
    .catch(err => {
      mostrarAlerta('error', 'Error al entregar algunas donaciones.');
      cargarDatos();
    });
  };

  const styles = {
    container: {
      padding: '5%',
      maxWidth: '1300px',
      margin: '0 auto',
      fontFamily: "'Inter', 'Roboto', sans-serif",
      backgroundColor: darkMode ? '#1B1D21' : '#F8F9FA',
      color: darkMode ? '#FFFFFF' : '#2A2D34',
      minHeight: '100vh',
      transition: 'all 0.3s ease'
    },
    header: {
      color: '#009DDC',
      marginBottom: '0.5rem',
      fontSize: '2.8rem',
      fontWeight: '800',
      letterSpacing: '-1px'
    },
    subtitle: {
      color: darkMode ? '#B0B7C6' : '#5C6475',
      marginBottom: '3rem',
      fontSize: '1.2rem',
      fontWeight: '400'
    },
    card: {
      backgroundColor: darkMode ? '#2A2D34' : '#FFFFFF',
      borderRadius: '16px',
      padding: '2.5rem',
      boxShadow: darkMode ? '0 10px 30px rgba(0,0,0,0.4)' : '0 10px 30px rgba(0,0,0,0.08)',
      marginBottom: '2.5rem',
      transition: 'all 0.3s ease',
      border: `1px solid ${darkMode ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.02)'}`
    },
    cardTitle: {
      color: darkMode ? '#FFFFFF' : '#2A2D34',
      marginBottom: '1.75rem',
      borderBottom: `2.5px solid ${darkMode ? 'rgba(255, 255, 255, 0.1)' : 'rgba(42, 45, 52, 0.1)'}`,
      paddingBottom: '1rem',
      fontSize: '1.3rem',
      letterSpacing: '-0.3px'
    },
    formGroup: {
      marginBottom: '1.5rem'
    },
    label: {
      display: 'block',
      fontWeight: '700',
      marginBottom: '0.5rem',
      color: darkMode ? '#FFFFFF' : '#2A2D34',
      fontSize: '0.85rem',
      textTransform: 'uppercase',
      letterSpacing: '0.5px'
    },
    input: {
      width: '100%',
      padding: '0.85rem 1rem',
      borderRadius: '8px',
      border: `1.5px solid ${darkMode ? 'rgba(255, 255, 255, 0.15)' : 'rgba(42, 45, 52, 0.15)'}`,
      backgroundColor: darkMode ? '#1B1D21' : '#FFFFFF',
      color: darkMode ? '#FFFFFF' : '#2A2D34',
      boxSizing: 'border-box',
      outline: 'none',
      fontSize: '0.95rem',
      transition: 'all 0.3s ease'
    },
    buttonPrimary: {
      backgroundColor: '#009DDC',
      color: '#ffffff',
      padding: '0.9rem 1.75rem',
      borderRadius: '8px',
      border: 'none',
      fontWeight: '700',
      cursor: 'pointer',
      width: '100%',
      transition: 'all 0.3s ease',
      fontSize: '0.95rem',
      boxShadow: darkMode ? '0 4px 12px rgba(0, 0, 0, 0.4)' : '0 4px 12px rgba(0, 157, 220, 0.2)'
    },
    buttonAccent: {
      backgroundColor: '#F26430',
      color: '#ffffff',
      padding: '0.6rem 1.25rem',
      borderRadius: '8px',
      border: 'none',
      fontWeight: '700',
      cursor: 'pointer',
      fontSize: '0.85rem',
      transition: 'all 0.3s ease',
      boxShadow: darkMode ? '0 4px 10px rgba(0, 0, 0, 0.4)' : '0 4px 10px rgba(242, 100, 48, 0.2)'
    },
    buttonAction: {
      backgroundColor: darkMode ? 'rgba(0, 157, 220, 0.15)' : 'rgba(0, 157, 220, 0.12)',
      color: '#009DDC',
      padding: '0.6rem 1.25rem',
      borderRadius: '8px',
      border: '1.5px solid #009DDC',
      fontWeight: '700',
      cursor: 'pointer',
      fontSize: '0.85rem',
      transition: 'all 0.3s ease'
    },
    table: {
      width: '100%',
      borderCollapse: 'collapse',
      marginTop: '1.25rem',
      transition: 'all 0.3s ease'
    },
    th: {
      backgroundColor: '#009DDC',
      color: '#ffffff',
      padding: '1.1rem',
      textAlign: 'left',
      fontWeight: '700',
      borderBottom: '2.5px solid #009DDC',
      fontSize: '0.9rem',
      textTransform: 'uppercase',
      letterSpacing: '0.5px',
      transition: 'all 0.3s ease'
    },
    td: {
      padding: '1.1rem',
      borderBottom: `1.5px solid ${darkMode ? 'rgba(255, 255, 255, 0.15)' : 'rgba(42, 45, 52, 0.15)'}`,
      color: darkMode ? '#B0B7C6' : '#5C6475',
      fontSize: '0.95rem',
      transition: 'all 0.3s ease'
    }
  };

  if (isServiceUnavailable) return <ServiceUnavailable darkMode={darkMode} />;

  return (
    <div style={styles.container}>
      {isLoading && (
        <div style={{
          position: 'fixed', top: 0, left: 0, width: '100%', height: '100%',
          backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 9999,
          display: 'flex', justifyContent: 'center', alignItems: 'center'
        }}>
          <div style={{
            width: '50px', height: '50px', border: '5px solid #F8F9FA',
            borderTop: '5px solid #009DDC', borderRadius: '50%',
            animation: 'spin 1s linear infinite'
          }}></div>
          <style>{`
            @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
          `}</style>
        </div>
      )}

      <h2 style={styles.header}>Donaton</h2>
      <p style={styles.subtitle}>
        Plataforma distribuida para la gestión y coordinación de ayuda humanitaria
      </p>

      {alertInfo.show && (
        <div style={{
          padding: '1rem 1.5rem',
          borderRadius: '8px',
          marginBottom: '2rem',
          fontWeight: '600',
          display: 'flex',
          alignItems: 'center',
          gap: '10px',
          backgroundColor: alertInfo.type === 'success' ? (darkMode ? 'rgba(0, 157, 220, 0.15)' : '#F8F9FA') : 'rgba(242, 100, 48, 0.1)',
          color: alertInfo.type === 'success' ? '#009DDC' : '#F26430',
          border: `1px solid ${alertInfo.type === 'success' ? '#009DDC' : '#F26430'}`,
          boxShadow: '0 2px 4px rgba(0,0,0,0.05)',
          transition: 'all 0.3s ease'
        }}>
          <span>{alertInfo.type === 'success' ? '✔' : '⚠'}</span>
          {alertInfo.message}
        </div>
      )}

      <div style={{ display: 'flex', gap: '1rem', marginBottom: '2.5rem', borderBottom: `2px solid ${darkMode ? 'rgba(255, 255, 255, 0.15)' : 'rgba(42, 45, 52, 0.15)'}`, paddingBottom: '1rem', transition: 'all 0.3s ease' }}>
        {[
          { id: 'donante', label: 'Vista Donante', icon: '🎁' },
          { id: 'voluntariado', label: 'Vista Voluntariado', icon: '🤝' },
          { id: 'admin', label: 'Vista Administrador', icon: '⚙️' }
        ].map(tab => (
          <button
            key={tab.id}
            style={{
              padding: '0.8rem 1.75rem',
              borderRadius: '30px',
              border: 'none',
              fontWeight: '700',
              cursor: 'pointer',
              backgroundColor: activeTab === tab.id ? '#009DDC' : (darkMode ? 'rgba(0, 157, 220, 0.12)' : 'rgba(0, 157, 220, 0.1)'),
              color: activeTab === tab.id ? '#ffffff' : (darkMode ? '#B0B7C6' : '#2A2D34'),
              transition: 'all 0.3s ease',
              boxShadow: activeTab === tab.id ? (darkMode ? '0 6px 16px rgba(0, 0, 0, 0.4)' : '0 6px 16px rgba(0, 157, 220, 0.3)') : 'none',
              display: 'flex',
              alignItems: 'center',
              gap: '8px',
              fontSize: '0.95rem'
            }}
            onClick={() => setActiveTab(tab.id)}
          >
            <span>{tab.icon}</span> {tab.label}
          </button>
        ))}
      </div>

      {activeTab === 'donante' && (
        <DonorLanding
          campanas={campanas}
          resumenCampanas={resumenCampanas}
          formDonacion={formDonacion}
          setFormDonacion={setFormDonacion}
          manejarDonacionSubmit={manejarDonacionSubmit}
          styles={styles}
          darkMode={darkMode}
        />
      )}

      {activeTab === 'voluntariado' && (
        <VolunteerView
          campanas={campanas}
          formVoluntario={formVoluntario}
          setFormVoluntario={setFormVoluntario}
          manejarVoluntarioSubmit={manejarVoluntarioSubmit}
          styles={styles}
          darkMode={darkMode}
        />
      )}

      {activeTab === 'admin' && (
        token ? (
          <AdminView
            campanas={campanas}
            donaciones={donaciones}
            formCampana={formCampana}
            setFormCampana={setFormCampana}
            manejarCampanaSubmit={manejarCampanaSubmit}
            cambiarEstadoCampana={cambiarEstadoCampana}
            cambiarEstadoDonacion={cambiarEstadoDonacion}
            manejarCampanaDelete={manejarCampanaDelete}
            manejarCampanaUpdate={manejarCampanaUpdate}
            manejarDonacionDelete={manejarDonacionDelete}
            editandoCampanaId={editandoCampanaId}
            setEditandoCampanaId={setEditandoCampanaId}
            formEditCampana={formEditCampana}
            setFormEditCampana={setFormEditCampana}
            styles={styles}
            onLogout={manejarLogout}
            darkMode={darkMode}
            voluntariosMap={voluntariosMap}
            despacharCampana={despacharCampana}
            entregarCampana={entregarCampana}
          />
        ) : (
          <div style={{ maxWidth: '450px', margin: '2rem auto', ...styles.card }}>
            <h3 style={styles.cardTitle}>Iniciar Sesión Administrador</h3>
            <form onSubmit={manejarLoginSubmit}>
              <div style={styles.formGroup}>
                <label style={styles.label}>Usuario</label>
                <input
                  type="text"
                  style={styles.input}
                  required
                  placeholder="Ingrese usuario"
                  value={username}
                  onChange={e => setUsername(e.target.value)}
                />
              </div>
              <div style={styles.formGroup}>
                <label style={styles.label}>Contraseña</label>
                <input
                  type="password"
                  style={styles.input}
                  required
                  placeholder="Ingrese contraseña"
                  value={password}
                  onChange={e => setPassword(e.target.value)}
                />
              </div>
              <button type="submit" style={{ ...styles.buttonPrimary, marginTop: '1rem' }}>
                Ingresar
              </button>
            </form>
          </div>
        )
      )}
    </div>
  );
};

export default Dashboard;
