package cl.duoc.donaton.ms.factory;

import cl.duoc.donaton.ms.model.Campana;

public class CampanaFactory {

    public static Campana crearCampanaNueva(String nombre, String descripcion) {
        Campana campana = new Campana();
        campana.setNombre(nombre);
        campana.setDescripcion(descripcion);
        campana.setEstado("PLANNED");
        return campana;
    }
}
