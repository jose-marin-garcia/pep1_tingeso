import axios from "axios";

const tingesoPep1Server = import.meta.env.VITE_PEP1_BACKEND_SERVER;
const tingesoPep1Port = import.meta.env.VITE_PEP1_BACKEND_PORT;

console.log(tingesoPep1Server)
console.log(tingesoPep1Port)

export default axios.create({
    baseURL: `http://${tingesoPep1Server}:${tingesoPep1Port}`,
    headers: {
        'Content-Type': 'application/json'
    }
});