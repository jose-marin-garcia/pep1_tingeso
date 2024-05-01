import React, { useState, useEffect } from 'react';
import reportesService from '../services/reportes.service';

const AverageTimeByMarks = () => {
    const [AverageTimeByMarks, setAverageTimeByMarks] = useState([]);

    useEffect(() => {
        reportesService
            .getReport3()
            .then((reponse) => {
                console.log("Mostrando resumen de reparaciones.", reponse.data);
                setAverageTimeByMarks(reponse.data);
            })
            .catch((error) => {
                console.log("Se ha producido un error al intentar mostrar Reporte 2.", error);
            });
    }, []);

    return (
        <div>
            <h1>Tiempo promedio de Reparación por Marcas (R3)</h1>
            <table>
                <thead>
                    <tr>
                        <th>Marcas</th>
                        <th>Tiempo promedio</th>
                    </tr>
                </thead>
                <tbody>
                    {AverageTimeByMarks.map((cost, index) => (
                        <tr key={index}>
                            <td>{cost.mark}</td>
                            <td>{cost.averageTime}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AverageTimeByMarks;
