import httpClient from "../http-common";

const getReport1 = () => {
    return httpClient.get('/reportes/costos-vehiculos');
}

const getReport2 = () => {
    return httpClient.get('/reportes/reparaciones-resumen');
}

const getReport3 = () => {
    return httpClient.get('');
}

const getReport4 = () => {
    return httpClient.get('/reportes/costos-vehiculos-marcas');
}

export default { getReport1, getReport2, getReport3, getReport4 };