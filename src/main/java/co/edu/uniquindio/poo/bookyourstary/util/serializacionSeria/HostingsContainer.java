package co.edu.uniquindio.poo.bookyourstary.util.serializacionSeria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import co.edu.uniquindio.poo.bookyourstary.model.Hosting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostingsContainer implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Hosting> hostings;
    
    private static HostingsContainer instance;
    
    private HostingsContainer() {
        this.hostings = new ArrayList<>();
    }
    
    public static HostingsContainer getInstance() {
        if (instance == null) {
            instance = new HostingsContainer();
        }
        return instance;
    }
    
    public void addHosting(Hosting hosting) {
        if (!hostings.contains(hosting)) {
            hostings.add(hosting);
        }
    }

    public void setHostings(List<Hosting> hostings) {
        this.hostings = new ArrayList<>(hostings);
    }
}
