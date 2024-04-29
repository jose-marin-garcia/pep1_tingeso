import { useState, useEffect } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import SaveIcon from "@mui/icons-material/Save";
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import registerService from "../services/register.service.js";
import OutlinedInput from '@mui/material/OutlinedInput';

const AddEditRegister = () => {
    const [patent, setPatent] = useState("");
    const [mark, setMark] = useState("");
    const [model, setModel] = useState("");
    const [type, setType] = useState("");
    const [year, setYear] = useState("");
    const [typemotor, setTypemotor] = useState("");
    const [numberseats, setNumberseats] = useState("");
    const [kilometers, setKilometers] = useState("");
    const [repairs, setRepairs] = useState([]);
    const { id } = useParams();
    const [titleRegisterForm, setTitleRegisterForm] = useState("");
    const navigate = useNavigate();

    const [reparaciones, setRepatraciones] = useState([]);
    const [marcas, setMarcas] = useState([]);

    const handleChange = (event) => {
        setType(event.target.value);
    };

    const init = () => {
        registerService
            .getMarks()
            .then((response) => {
                console.log("Mostrando marcas.", response.data);
                setMarcas(response.data);
            })
            .catch((error) => {
                console.log(
                    "Se ha producido un error al intentar mostrar marcas.",
                    error
                );
            });
        registerService
            .getRepairs()
            .then((response) => {
                console.log("Mostrando reparaciones.", response.data);
                setRepatraciones(response.data);
            })
            .catch((error) => {
                console.log(
                    "Se ha producido un error al intentar mostrar reparaciones.",
                    error
                );
            });
    };

    const saveRegister = async (e) => {
        e.preventDefault();

        const vehicle = { id, patent, mark, model, type, year, typemotor, numberseats, kilometers };
        if (id) {
            //Actualizar Datos
            registerService
                .update(vehicle, repairs)
                .then((response) => {
                    console.log("Registro ha sido actualizado.", response.data);
                    navigate("/registro");
                })
                .catch((error) => {
                    console.log(
                        "Ha ocurrido un error al intentar actualizar datos del registro.",
                        error
                    );
                });
        } else {
            //Crear nuevo registro
            try {
                const response = await registerService.getBond(mark);
                if (response.ok) {
                    const bono = await response.json();
                    const confirm = window.confirm(`Hay un bono disponible para este vehículo de ${bono.amount}. ¿Desea aplicarlo?`);
                    if (confirm) {
                        await registrarConBono(vehicle, repairs, bono.id);
                    } else {
                        await registrarConBono(vehicle, repairs, null);
                    }
                } else {
                    console.log("No se encontró bono, procediendo sin aplicar bono.");
                    await registrarConBono(vehicle, repairs, null);
                }
            } catch (error) {
                console.error("Error al verificar bonos", error);
                await registrarConBono(vehicle, repairs, null);
            }
            
        }
    };

        const registrarConBono = async (vehicle, repairs, bono) => {
            try {
                const response = await registerService.create(vehicle, repairs, bono);
                console.log("Se ha registrado correctamente.", response.data);
                navigate("/registro");
            } catch (error) {
                console.log("Ha ocurrido un error al intentar crear nuevo registro.", error);
            }
        };

        useEffect(() => {
            if (id) {
                setTitleRegisterForm("Editar Registro");
                registerService
                    .get(id)
                    .then((register) => {
                        setPatent(register.data.patent);
                        setMarcaSeleccionada(register.data.mark);
                        setModel(register.data.model);
                        setType(register.data.type);
                        setYear(register.data.year);
                        setNumberseats(register.data.numberseats);
                        setKilometers(register.data.kilometers);
                        setRepairs(register.data.repairs);
                    })
                    .catch((error) => {
                        console.log("Se ha producido un error.", error);
                    });
            } else {
                setTitleRegisterForm("Nuevo Registro");
            }

            init();
        }, []); // Ejecutar efecto solo una vez al montar el componente

        return (
            <Box
                sx={{
                    display: 'static',
                    justifyContent: 'center',
                    alignItems: 'center',
                    flexDirection: 'column',
                    component: 'form',
                }}
            >
                <h3> {titleRegisterForm} </h3>
                <FormControl fullWidth>
                    <TextField
                        id="patent"
                        label="Patente"
                        value={patent}
                        variant="standard"
                        onChange={(e) => setPatent(e.target.value)}
                        helperText="Ej. ABCD12"
                    />
                </FormControl>

                <FormControl fullWidth>
                    <InputLabel id="marca-label">Marca</InputLabel>
                    <Select
                        id="mark"
                        value={mark}
                        onChange={(e) => setMark(e.target.value)}
                    >
                        {Array.isArray(marcas) && marcas.map(marca => (
                            <MenuItem key={marca.id} value={marca.id}>
                                {marca.markName}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <FormControl fullWidth>
                    <TextField
                        id="model"
                        label="Modelo"
                        value={model}
                        variant="standard"
                        onChange={(e) => setModel(e.target.value)}
                    />
                </FormControl>

                <FormControl fullWidth variant="standard">
                    <InputLabel id="type-label">Tipo de auto</InputLabel>
                    <Select
                        labelId="type-label"
                        id="type"
                        value={type}
                        onChange={(e) => setType(e.target.value)}
                        label="Tipo de auto"
                    >
                        <MenuItem value="Sedan">Sedan</MenuItem>
                        <MenuItem value="Hatchback">Hatchback</MenuItem>
                        <MenuItem value="SUV">SUV</MenuItem>
                        <MenuItem value="Pickup">Pickup</MenuItem>
                        <MenuItem value="Furgoneta">Furgoneta</MenuItem>
                    </Select>
                </FormControl>

                <FormControl fullWidth>
                    <DatePicker
                        label="Año de fabricación"
                        views={['year']}
                        onChange={(newDate) => {
                            const date = newDate instanceof Date ? newDate : new Date(newDate);
                            if (!isNaN(date.getFullYear())) {
                                const year = date.getFullYear();
                                setYear(year);
                                console.log("Año seleccionado:", year); // Esto te ayudará a verificar que el año se esté actualizando correctamente
                            }
                        }}
                        style={{ marginBottom: '100px' }}
                    />
                </FormControl>


                <FormControl fullWidth variant="standard">
                    <InputLabel id="typemotor-label">Tipo de motor</InputLabel>
                    <Select
                        labelId="typemotor-label"
                        id="typemotor"
                        value={typemotor}
                        onChange={(e) => setTypemotor(e.target.value)}
                        label="Tipo de motor"
                    >
                        <MenuItem value="Gasolina">Gasolina</MenuItem>
                        <MenuItem value="Diesel">Diesel</MenuItem>
                        <MenuItem value="Hbrido">Híbrido</MenuItem>
                        <MenuItem value="Eléctrico">Eléctrico</MenuItem>
                    </Select>
                </FormControl>

                <FormControl fullWidth>
                    <TextField
                        id="numberseats"
                        label="Número de asientos"
                        value={numberseats}
                        type="number"
                        variant="standard"
                        onChange={(e) => setNumberseats(e.target.value)}
                    />
                </FormControl>

                <FormControl fullWidth>
                    <TextField
                        id="kilometers"
                        label="Kilómetros"
                        value={kilometers}
                        type="number"
                        variant="standard"
                        onChange={(e) => setKilometers(e.target.value)}
                    />
                </FormControl>

                <FormControl fullWidth>
                    <Select
                        labelId="demo-multiple-checkbox-label"
                        id="demo-multiple-checkbox"
                        multiple
                        value={repairs}
                        label="Reparaciones"
                        onChange={(e) => setRepairs(e.target.value)}
                        input={<OutlinedInput label="Tag" />}
                    >
                        {Array.isArray(reparaciones) && reparaciones.map(reparacion => (
                            <MenuItem key={reparacion.id} value={reparacion.id}>
                                {reparacion.repairName}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <FormControl fullWidth>
                    <br />
                    <Button
                        variant="contained"
                        color="info"
                        onClick={(e) => saveRegister(e)}
                        style={{ marginLeft: "0.5rem" }}
                        startIcon={<SaveIcon />}
                    >
                        Grabar
                    </Button>
                </FormControl>
                <Link to="/registro">Back to List</Link>
            </Box >
        );
    };

    export default AddEditRegister;
