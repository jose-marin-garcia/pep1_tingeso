import React, { useState, useEffect } from 'react';
import reportesService from '../services/reportes.service';

const ReparacionesResumen = () => {
    const [ReparacionesResumen, setReparacionesResumen] = useState([]);

    useEffect(() => {
        reportesService
            .getReport2()
            .then((reponse) => {
                console.log("Mostrando resumen de reparaciones.", reponse.data);
                setReparacionesResumen(reponse.data);
            })
            .catch((error) => {
                console.log("Se ha producido un error al intentar mostrar Reporte 2.", error);
            });
    }, []);

    return (
        <div>
            <h1>Resumen de Reparaciones (R2)</h1>
            <table>
                <thead>
                    <tr>
                        <th>Nombre de Reparación</th>
                        <th>Sedan</th>
                        <th>Hatchback</th>
                        <th>SUV</th>
                        <th>Pickup</th>
                        <th>Furgoneta</th>
                        <th>Monto Total</th>
                    </tr>
                </thead>
                <tbody>
                    {ReparacionesResumen.map((cost, index) => (
                        <tr key={index}>
                            <td>{cost.typeRepairName}</td>
                            <td>{cost.sedanCount}</td>
                            <td>{cost.hatchbackCount}</td>
                            <td>{cost.suvCount}</td>
                            <td>{cost.pickupCount}</td>
                            <td>{cost.furgonetaCount}</td>
                            <td>{cost.totalCost}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ReparacionesResumen;
