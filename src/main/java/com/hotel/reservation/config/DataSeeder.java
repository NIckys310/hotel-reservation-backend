package com.hotel.reservation.config;

import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomType;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** Loads the 16 predefined rooms the first time the database is empty. */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final String IMG = "https://images.unsplash.com/photo-%s?auto=format&fit=crop&w=1200&q=80";

    private static final List<String> SINGLE_AMENITIES =
            List.of("Wi-Fi de alta velocidad", "Smart TV 43\"", "Aire acondicionado", "Escritorio de trabajo", "Caja fuerte");
    private static final List<String> DOUBLE_AMENITIES =
            List.of("Wi-Fi de alta velocidad", "Smart TV 50\"", "Aire acondicionado", "Minibar", "Cafetera Nespresso", "Caja fuerte");
    private static final List<String> SUITE_AMENITIES =
            List.of("Wi-Fi de alta velocidad", "Smart TV 65\"", "Sala de estar privada", "Bañera de hidromasaje",
                    "Minibar premium", "Cafetera Nespresso", "Batas y pantuflas", "Servicio de mayordomo");

    private final RoomRepository roomRepository;

    public DataSeeder(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {
        if (roomRepository.count() == 0) {
            roomRepository.saveAll(predefinedRooms());
        }
    }

    private static String image(String id) {
        return IMG.formatted(id);
    }

    static List<Room> predefinedRooms() {
        return List.of(
                new Room("101", RoomType.SINGLE, 1, "Sencilla Jardín", "Habitación tranquila con ventanal hacia los jardines interiores del hotel.",
                        22, "Cama queen", "Jardín", image("1611892440504-42a792e24d32"), SINGLE_AMENITIES),
                new Room("102", RoomType.SINGLE, 1, "Sencilla Ejecutiva", "Pensada para viajes de negocios, con escritorio amplio e iluminación cálida.",
                        20, "Cama queen", "Patio interior", image("1566665797739-1674de7a421a"), SINGLE_AMENITIES),
                new Room("103", RoomType.SINGLE, 1, "Sencilla Terraza", "Acceso directo a una terraza compartida con sillas de descanso.",
                        24, "Cama queen", "Terraza", image("1562438668-bcf0ca6578f0"), SINGLE_AMENITIES),
                new Room("104", RoomType.DOUBLE, 1, "Doble Clásica", "Dos camas semidobles con ropa de cama de algodón egipcio.",
                        30, "2 camas semidobles", "Jardín", image("1568495248636-6432b97bd949"), DOUBLE_AMENITIES),
                new Room("105", RoomType.DOUBLE, 1, "Doble Superior", "Cama king con cabecero de madera y zona de lectura.",
                        32, "Cama king", "Jardín", image("1582719478250-c89cae4dc85b"), DOUBLE_AMENITIES),
                new Room("201", RoomType.SINGLE, 2, "Sencilla Ciudad", "Vista a la ciudad y cortinas blackout para un descanso perfecto.",
                        22, "Cama queen", "Ciudad", image("1578898886225-c7c894047899"), SINGLE_AMENITIES),
                new Room("202", RoomType.SINGLE, 2, "Sencilla Silenciosa", "Ubicada en el ala más silenciosa del hotel, ideal para descansar.",
                        21, "Cama queen", "Patio interior", image("1629140727571-9b5c6f6267b4"), SINGLE_AMENITIES),
                new Room("203", RoomType.DOUBLE, 2, "Doble Balcón", "Balcón privado con mesa para dos y vista a la piscina.",
                        34, "Cama king", "Piscina", image("1631049307264-da0ec9d70304"), DOUBLE_AMENITIES),
                new Room("204", RoomType.DOUBLE, 2, "Doble Piscina", "Ventanales de piso a techo frente a la piscina principal.",
                        33, "Cama king", "Piscina", image("1618773928121-c32242e63f39"), DOUBLE_AMENITIES),
                new Room("205", RoomType.DOUBLE, 2, "Doble Familiar", "Espacio amplio con sofá cama, ideal para familias pequeñas.",
                        38, "Cama king + sofá cama", "Ciudad", image("1590490360182-c33d57733427"), DOUBLE_AMENITIES),
                new Room("301", RoomType.SINGLE, 3, "Sencilla Premium", "Acabados en madera, cafetera y amenidades de baño premium.",
                        25, "Cama queen", "Montañas", image("1576354302919-96748cb8299e"), SINGLE_AMENITIES),
                new Room("302", RoomType.DOUBLE, 3, "Doble Ejecutiva", "Acceso al salón ejecutivo y zona de trabajo independiente.",
                        35, "Cama king", "Montañas", image("1505693416388-ac5ce068fe85"), DOUBLE_AMENITIES),
                new Room("303", RoomType.DOUBLE, 3, "Doble Jacuzzi", "Jacuzzi privado junto a la ventana con vista a las montañas.",
                        40, "Cama king", "Montañas", image("1631049421450-348ccd7f8949"), DOUBLE_AMENITIES),
                new Room("401", RoomType.SUITE, 4, "Suite Junior", "Suite con sala de estar separada y sofá frente al ventanal.",
                        55, "Cama king", "Ciudad", image("1578683010236-d716f9a3f461"), SUITE_AMENITIES),
                new Room("402", RoomType.SUITE, 4, "Suite Terraza", "Terraza privada con tumbonas y vista panorámica de la piscina.",
                        65, "Cama king", "Piscina", image("1564078516393-cf04bd966897"), SUITE_AMENITIES),
                new Room("501", RoomType.SUITE, 5, "Suite Presidencial", "La suite más exclusiva: comedor privado, dos baños y vista de 180°.",
                        95, "Cama super king", "Panorámica", image("1590381105924-c72589b9ef3f"), SUITE_AMENITIES));
    }
}
