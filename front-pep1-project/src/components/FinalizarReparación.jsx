import React, { useState, useEffect } from 'react';
import registerService from '../services/register.service';
import Button from '@mui/material/Button';
import DoneIcon from '@mui/icons-material/Done';

const FinalizarReparacion = () => {
    const [vehicles, setVehicles] = useState([]);

    useEffect(() => {
        registerService
            .getVehiclesNotFinished()
            .then((reponse) => {
                console.log("Mostrando vehículos.", reponse.data);
                setVehicles(reponse.data);
            })
            .catch((error) => {
                console.log("Se ha producido un error al intentar mostrar Reporte 1.", error);
            });
    }, []);

    const handleClick = (patent) => {
        registerService.finalizarReparacion(patent)
            .then(() => {
                // La reparación se ha finalizado correctamente, recargar la página
                window.location.reload();
            })
            .catch((error) => {
                // Manejo de errores en caso de que algo salga mal
                console.error('Error al finalizar la reparación:', error);
            });
    };
    

    return (
        <div>
            <h1>Finalizar Reparación</h1>
            <table>
                <thead>
                    <tr>
                        <th>Patente</th>
                        <th>Marca</th>
                        <th>Modelo</th>
                        <th>Tipo</th>
                        <th>Operación</th>
                    </tr>
                </thead>
                <tbody>
                    {vehicles.map((vehicle, index) => (
                        <tr key={index}>
                            <td>{vehicle.patent}</td>
                            <td>{vehicle.mark}</td>
                            <td>{vehicle.model}</td>
                            <td>{vehicle.type}</td>
                            <td>
                                <Button
                                    variant="contained"
                                    color="info"
                                    size="small"
                                    onClick={() => handleClick(vehicle.patent)}
                                    style={{ marginLeft: "0.5rem" }}
                                    startIcon={<DoneIcon />}
                                >
                                    Finalizar Reparación
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default FinalizarReparacion;
