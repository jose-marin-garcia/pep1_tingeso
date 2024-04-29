import React, { useState, useEffect } from 'react';
import reportesService from '../services/reportes.service';

const ReparacionesResumen = () => {
    const [ReparacionesResumen, setReparacionesResumen] = useState([]);

    useEffect(() => {
        reportesService
            .getReport4()
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
            <h1>Resumen de Reparaciones por Tipo de Motor (R4)</h1>
            <table>
                <thead>
                    <tr>
                        <th>Nombre de Reparación</th>
                        <th>Gasolina</th>
                        <th>Diesel</th>
                        <th>Híbrido</th>
                        <th>Eléctrico</th>
                        <th>Monto Total</th>
                    </tr>
                </thead>
                <tbody>
                    {ReparacionesResumen.map((cost, index) => (
                        <tr key={index}>
                            <td>{cost.typeRepairName}</td>
                            <td>{cost.gasolineCount}</td>
                            <td>{cost.dieselCount}</td>
                            <td>{cost.hybridCount}</td>
                            <td>{cost.electricCount}</td>
                            <td>{cost.totalCost}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ReparacionesResumen;
