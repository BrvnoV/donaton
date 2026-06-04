import React from 'react';

const StatusAlert = ({ data, defaultMessage }) => {
  // Si la data es un String, significa que el BFF activó el Fallback por caída del microservicio
  if (typeof data === 'string') {
    return (
      <div className="alert-fallback">
           <strong>Modo Resiliente:</strong> {data}
      </div>
    );
  }
  
  // Si los datos no cargan o la promesa falla por completo
  if (!data) {
    return (
      <div className="alert-fallback">
           {defaultMessage || "Conexión interrumpida con el servidor de origen."}
      </div>
    );
  }

  return null;
};

export default StatusAlert;