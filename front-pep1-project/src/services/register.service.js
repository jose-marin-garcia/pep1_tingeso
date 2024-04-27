import httpClient from "../http-common";

const getMarks = () => {
    return httpClient.get('/marks/');
}

const getRepairs = () => {
    return httpClient.get('/typerepairs/')
}

const create = (vehicle, reparations) => {
    const data = { vehicle, reparations };
    return httpClient.post('/register/', data);
}

const update = (vehicle, repairs) => {
    const data = { vehicle: vehicle, reparations: repairs };
    return httpClient.put('/register/', data);
}

export default { getMarks, getRepairs, create, update };