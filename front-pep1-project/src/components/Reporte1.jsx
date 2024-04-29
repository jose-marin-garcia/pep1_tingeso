import React, { useState, useEffect } from 'react';
import reportesService from '../services/reportes.service';

const VehicleCosts = () => {
    const [vehicleCosts, setVehicleCosts] = useState([]);

    useEffect(() => {
        reportesService
            .getReport1()
            .then((reponse) => {
                console.log("Mostrando costos de vehículos.", reponse.data);
                setVehicleCosts(reponse.data);
            })
            .catch((error) => {
                console.log("Se ha producido un error al intentar mostrar Reporte 1.", error);
            });
    }, []);

    return (
        <div>
            <h1>Costos de Reparaciones (R1)</h1>
            <table>
                <thead>
                    <tr>
                        <th>Patente</th>
                        <th>Marca</th>
                        <th>Modelo</th>
                        <th>Tipo</th>
                        <th>Suma de Reparaciones</th>
                        <th>Recargos</th>
                        <th>Descuentos</th>
                        <th>IVA</th>
                        <th>Costo Total</th>
                    </tr>
                </thead>
                <tbody>
                    {vehicleCosts.map((cost, index) => (
                        <tr key={index}>
                            <td>{cost.patent}</td>
                            <td>{cost.marca}</td>
                            <td>{cost.modelo}</td>
                            <td>{cost.tipoVehiculo}</td>
                            <td>{cost.sumaReparaciones}</td>
                            <td>{cost.recargos}</td>
                            <td>{cost.descuentos}</td>
                            <td>{cost.iva}</td>
                            <td>{cost.montoTotal}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default VehicleCosts;
