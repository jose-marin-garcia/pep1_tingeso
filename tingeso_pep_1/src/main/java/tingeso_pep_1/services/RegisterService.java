package tingeso_pep_1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso_pep_1.DataTransferObjects.VehicleDto;
import tingeso_pep_1.entities.BondEntity;
import tingeso_pep_1.entities.HistoricEntity;
import tingeso_pep_1.entities.HistoryRepairsEntity;
import tingeso_pep_1.entities.VehicleEntity;
import tingeso_pep_1.repositories.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegisterService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    HistoricRepository historicRepository;

    @Autowired
    HistoryRepairsRepository historyRepairsRepository;

    @Autowired
    PricesRepository pricesRepository;

    @Autowired
    BondRepository bondRepository;

    @Autowired
    MarksRepository markRepository;

    public VehicleEntity saveVehicle(VehicleEntity vehiculo, List<Long> reparaciones, Long idBond) {
        System.out.println("ID Bond recibido: " + idBond);
        LocalDateTime fechaHora = LocalDateTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        String fechaActual = fechaHora.format(formatoFecha);
        String horaActual = fechaHora.format(formatoHora);

        int sumaReparaciones = calcularSumaReparaciones(vehiculo, reparaciones, horaActual);

        int bono = 0;

        if (idBond != null) {
            bono = bondRepository.findById(idBond).get().getAmount();
            bondRepository.deleteById(idBond);
        }

        int descuentos = ((int) (sumaReparaciones * calcularDescuentos(vehiculo.getTypemotor(), reparaciones.size(), fechaActual, horaActual))) + bono;

        int recargos = (int) (sumaReparaciones * calculoRecargos(vehiculo.getKilometers(), vehiculo.getType(), fechaHora.getYear()-vehiculo.getYear()));

        int montoTotal = sumaReparaciones - descuentos + recargos;

        int iva = montoTotal * 19 / 100;

        HistoricEntity historial = historicRepository.save(new HistoricEntity(null, vehiculo.getPatent(), fechaActual, horaActual, montoTotal + iva, sumaReparaciones, descuentos, recargos, iva, null, null, null, null));

        Long idHistorial = historial.getId();

        for (Long idReparacion : reparaciones) {
            historyRepairsRepository.save(new HistoryRepairsEntity(null, idHistorial, idReparacion));
        }
        return vehicleRepository.save(vehiculo);
    }

    public int calcularSumaReparaciones(VehicleEntity vehiculo, List<Long> reparaciones, String hora){
        int montoTotal = 0;

        // Obtener el tipo de motor para aplicar los precios base
        String motortype = vehiculo.getTypemotor();

        // Iterar sobre la lista de IDs de reparación para sumar los costos base
        for (Long idReparacion : reparaciones) {
            // Aquí necesitarás una forma de mapear idReparacion a un tipo de reparación y luego a un costo.
            // Esto podría hacerse mediante una consulta a la base de datos o un mapa en memoria, por ejemplo.
            int costoReparacion = pricesRepository.findPriceByIdtyperepairAndMotortype(idReparacion, motortype);
            montoTotal += costoReparacion;
        }

        return montoTotal;
    }

    public double calcularDescuentos(String motorType, int totalRepairs, String fecha, String hora) {
        double descuento = calcularDescuentoPorNumeroDeReparaciones(motorType, totalRepairs) + aplicarDescuentoSiCorresponde(fecha,hora);
        //imprimir descuento por consola
        System.out.println("Descuento: " + descuento);
        return descuento;
    }

    public double calcularDescuentoPorNumeroDeReparaciones(String motorType, int totalRepairs) {
        double descuento = 0;
        switch (motorType) {
            case "Gasolina":
                descuento = calculateDiscountForGasoline(totalRepairs);
                break;
            case "Diesel":
                descuento = calculateDiscountForDiesel(totalRepairs);
                break;
            case "Hibrido":
                descuento = calculateDiscountForHibrid(totalRepairs);
                break;
            case "Electrico":
                descuento = calculateDiscountForHybridElectric(totalRepairs);
                break;
            default:
                break;
        }
        return descuento;
    }

    private double calculateDiscountForGasoline(int totalRepairs) {
        if (totalRepairs >= 1 && totalRepairs <= 2) {
            return 0.05;
        } else if (totalRepairs >= 3 && totalRepairs <= 5) {
            return 0.10;
        } else if (totalRepairs >= 6 && totalRepairs <= 9) {
            return 0.15;
        } else if (totalRepairs >= 10) {
            return 0.20;
        }
        return 0.0;
    }

    private double calculateDiscountForDiesel(int totalRepairs) {
        if (totalRepairs >= 1 && totalRepairs <= 2) {
            return 0.07;
        } else if (totalRepairs >= 3 && totalRepairs <= 5) {
            return 0.12;
        } else if (totalRepairs >= 6 && totalRepairs <= 9) {
            return 0.17;
        } else if (totalRepairs >= 10) {
            return 0.22;
        }
        return 0.0;
    }

    private double calculateDiscountForHibrid(int totalRepairs) {
        if (totalRepairs >= 1 && totalRepairs <= 2) {
            return 0.10;
        } else if (totalRepairs >= 3 && totalRepairs <= 5) {
            return 0.15;
        } else if (totalRepairs >= 6 && totalRepairs <= 9) {
            return 0.20;
        } else if (totalRepairs >= 10) {
            return 0.25;
        }
        return 0.0;
    }

    private double calculateDiscountForHybridElectric(int totalRepairs) {
        if (totalRepairs >= 1 && totalRepairs <= 2) {
            return 0.08;
        } else if (totalRepairs >= 3 && totalRepairs <= 5) {
            return 0.13;
        } else if (totalRepairs >= 6 && totalRepairs <= 9) {
            return 0.18;
        } else if (totalRepairs >= 10) {
            return 0.23;
        }
        return 0.0;
    }

    public double aplicarDescuentoSiCorresponde(String fecha, String hora) {
        if (esDescuentoAplicable(fecha, hora)) {
            return 0.1;
        }
        return 0.0;
    }

    public boolean esDescuentoAplicable(String fecha, String hora) {

        // Combina fecha y hora en un solo objeto LocalDateTime
        LocalDateTime fechaHora = LocalDateTime.parse(fecha + " " + hora, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        if(fechaHora.getDayOfWeek() == DayOfWeek.MONDAY || fechaHora.getDayOfWeek() == DayOfWeek.THURSDAY){
            LocalDateTime inicioDescuento = fechaHora.withHour(9).withMinute(0);
            LocalDateTime finDescuento = fechaHora.withHour(12).withMinute(0);
            return fechaHora.isAfter(inicioDescuento) && fechaHora.isBefore(finDescuento);
        }else{
            return false;
        }
    }

    public double calculoRecargos(int kilometers, String type, int antiguedad) {
        return calcularRecargoPorKilometraje(kilometers,type) + calcularRecargoPorAntiguedad(antiguedad,type);
    }

    private double calcularRecargoPorKilometraje(int kilometraje, String tipoAuto) {

        return switch (tipoAuto) {
            case "Sedan", "Hatchback" -> calcularRecargoPorTipo(kilometraje, 0.03, 0.07);
            case "SUV", "Pickup", "Furgoneta" -> calcularRecargoPorTipo(kilometraje, 0.05, 0.09);
            default -> throw new IllegalArgumentException("Tipo de auto desconocido: " + tipoAuto);
        };
    }

    private double calcularRecargoPorTipo(int kilometraje, double recargo1, double recargo2) {
        if (kilometraje <= 5000) {
            return 0;
        } else if (kilometraje <= 12000) {
            return recargo1;
        } else if (kilometraje <= 25000) {
            return recargo2;
        } else if (kilometraje <= 40000) {
            return 0.12;
        } else {
            return 0.2;
        }
    }

    private double calcularRecargoPorAntiguedad(int antiguedad, String tipoAuto) {
        return switch (tipoAuto) {
            case "Sedan", "Hatchback" -> calcularRecargoPorTipoAntiguedad(antiguedad, 0.05, 0.09, 0.15);
            case "SUV", "Pickup", "Furgoneta" -> calcularRecargoPorTipoAntiguedad(antiguedad, 0.07, 0.11, 0.20);
            default -> throw new IllegalArgumentException("Tipo de auto desconocido: " + tipoAuto);
        };
    }

    private double calcularRecargoPorTipoAntiguedad(int antiguedad , double recargo1, double recargo2, double recargo3) {
        if (antiguedad <= 5) {
            return 0;
        } else if (antiguedad <= 10) {
            return recargo1;
        } else if (antiguedad <= 15) {
            return recargo2;
        } else {
            return recargo3;
        }
    }

    public BondEntity getBond(Long id_mark) {
        if(id_mark == null)
            return null;
        System.out.println(bondRepository.findFirstByIdmark(id_mark));
        return bondRepository.findFirstByIdmark(id_mark);
    }

    public BondEntity addBond(Long idMark, int amount) {
        return bondRepository.save(new BondEntity(null, idMark, amount));
    }

    public List<VehicleDto> getVehiclesNotFinished() {
        List<HistoricEntity> historiales = historicRepository.findAllByEndhourIsNullAndEnddateIsNull();
        List<VehicleDto> vehicles = new ArrayList<>();
        for (HistoricEntity h : historiales) {
            VehicleEntity v = vehicleRepository.findByPatent(h.getPatent());
            VehicleDto vdto = new VehicleDto(v.getId(),v.getPatent(), markRepository.findById(v.getMark()).get().getMarkName(), v.getModel(), v.getType(), v.getYear(), v.getTypemotor(), v.getNumberseats(), v.getKilometers());
            vehicles.add(vdto);
        }
        return vehicles;
    }

    public VehicleEntity finishVehicle(String patent) {
        LocalDateTime fechaHora = LocalDateTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        String fechaActual = fechaHora.format(formatoFecha);
        String horaActual = fechaHora.format(formatoHora);

        HistoricEntity historial = historicRepository.findByPatent(patent);
        historial.setEnddate(fechaActual);
        historial.setEndhour(horaActual);
        historicRepository.save(historial);
        return vehicleRepository.findByPatent(patent);
    }

    public List<VehicleDto> getVehiclesNotRemoved() {
        List<HistoricEntity> historiales = historicRepository.findAllByEndhourIsNotNullAndEnddateIsNotNullAndClientdateIsNullAndClienthourIsNull();
        List<VehicleDto> vehicles = new ArrayList<>();
        for (HistoricEntity h : historiales) {
            VehicleEntity v = vehicleRepository.findByPatent(h.getPatent());
            VehicleDto vdto = new VehicleDto(v.getId(),v.getPatent(), markRepository.findById(v.getMark()).get().getMarkName(), v.getModel(), v.getType(), v.getYear(), v.getTypemotor(), v.getNumberseats(), v.getKilometers());
            vehicles.add(vdto);
        }
        return vehicles;
    }

    public VehicleEntity removeVehicle(String patent) {
        LocalDateTime fechaHora = LocalDateTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        String fechaActual = fechaHora.format(formatoFecha);
        String horaActual = fechaHora.format(formatoHora);

        HistoricEntity historial = historicRepository.findByPatent(patent);
        historial.setClientdate(fechaActual);
        historial.setClienthour(horaActual);
        historicRepository.save(historial);
        return vehicleRepository.findByPatent(patent);
    }

}
