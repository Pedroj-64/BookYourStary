package co.edu.uniquindio.poo.bookyourstary.repository;

public class StatisticsRepository {

    private static StatisticsRepository instance;

    private StatisticsRepository() {
        // Constructor privado para evitar instanciación externa
    }

    public static StatisticsRepository getInstance() {
        if (instance == null) {
            instance = new StatisticsRepository();
        }
        return instance;
    }

    

}
