package co.edu.uniquindio.poo.bookyourstary.internalControllers;

public class SessionManager {
    private static SessionManager instance;
    private Object usuarioActual;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    public void setUsuarioActual(Object usuario) {
        this.usuarioActual = usuario;
    }

    public Object getUsuarioActual() {
        return usuarioActual;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }
}
