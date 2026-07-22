import re

with open('donaton-front/src/views/Dashboard.jsx', 'r') as f:
    content = f.read()

# 1. Imports
content = content.replace(
    "import AdminView from '../components/AdminView';",
    "import AdminView from '../components/AdminView';\nimport clienteAxios, { interceptoresAxios } from '../api/clienteAxios';\nimport ServiceUnavailable from '../components/ServiceUnavailable';"
)

# 2. States
content = content.replace(
    "const [activeTab, setActiveTab] = useState('donante');",
    "const [activeTab, setActiveTab] = useState('donante');\n  const [isLoading, setIsLoading] = useState(false);\n  const [isServiceUnavailable, setServiceUnavailable] = useState(false);"
)

# 3. Interceptor setup inside a useEffect
content = content.replace(
    "const cargarDatos = () => {",
    "useEffect(() => {\n    interceptoresAxios(setIsLoading, setServiceUnavailable);\n  }, []);\n\n  const cargarDatos = () => {"
)

# 4. cargarDatos
content = re.sub(
    r"const cargarDatos = \(\) => \{.*?\};\n",
    """const cargarDatos = () => {
    clienteAxios.get('/campanas')
      .then(res => setCampanas(Array.isArray(res.data) ? res.data : []))
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
""",
    content,
    flags=re.DOTALL
)

# 5. manejarLoginSubmit
content = re.sub(
    r"const manejarLoginSubmit = \(e\) => \{.*?\};\n",
    """const manejarLoginSubmit = (e) => {
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
""",
    content,
    flags=re.DOTALL
)

# 6. manejarDonacionSubmit
content = re.sub(
    r"const manejarDonacionSubmit = \(e\) => \{.*?\};\n",
    """const manejarDonacionSubmit = (e) => {
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
""",
    content,
    flags=re.DOTALL
)

# 7. manejarVoluntarioSubmit
content = re.sub(
    r"const manejarVoluntarioSubmit = \(e\) => \{.*?\};\n",
    """const manejarVoluntarioSubmit = (e) => {
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
        mostrarAlerta('success', '¡Excelente! Te has registrado como voluntario de la campaña.');
        setFormVoluntario({ nombre: '', contacto: '', correo: '', campanaId: '' });
        cargarDatos();
      })
      .catch(err => {
        mostrarAlerta('error', err.response?.data?.message || err.message);
      });
  };
""",
    content,
    flags=re.DOTALL
)

# 8. manejarCampanaSubmit
content = re.sub(
    r"const manejarCampanaSubmit = \(e\) => \{.*?\};\n",
    """const manejarCampanaSubmit = (e) => {
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
""",
    content,
    flags=re.DOTALL
)

# 9. cambiarEstadoDonacion
content = re.sub(
    r"const cambiarEstadoDonacion = \(id, nuevoEstado\) => \{.*?\};\n",
    """const cambiarEstadoDonacion = (id, nuevoEstado) => {
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
""",
    content,
    flags=re.DOTALL
)

# 10. cambiarEstadoCampana
content = re.sub(
    r"const cambiarEstadoCampana = \(id, nuevoEstado\) => \{.*?\};\n",
    """const cambiarEstadoCampana = (id, nuevoEstado) => {
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
""",
    content,
    flags=re.DOTALL
)

# 11. manejarCampanaDelete
content = re.sub(
    r"const manejarCampanaDelete = \(id\) => \{.*?\};\n",
    """const manejarCampanaDelete = (id) => {
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
""",
    content,
    flags=re.DOTALL
)

# 12. manejarCampanaUpdate
content = re.sub(
    r"const manejarCampanaUpdate = \(e\) => \{.*?\};\n",
    """const manejarCampanaUpdate = (e) => {
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
""",
    content,
    flags=re.DOTALL
)

# 13. Render ServiceUnavailable if needed
content = content.replace(
    "return (\n    <div style={styles.container}>",
    "if (isServiceUnavailable) return <ServiceUnavailable darkMode={darkMode} />;\n\n  return (\n    <div style={styles.container}>"
)

# 14. Loading Spinner logic
# I'll add a simple overlay spinner when isLoading is true.
spinner_jsx = """      {isLoading && (
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

      {/* Banner de retroalimentación */}"""
content = content.replace("{/* Banner de retroalimentación */}", spinner_jsx)

with open('donaton-front/src/views/Dashboard.jsx', 'w') as f:
    f.write(content)
