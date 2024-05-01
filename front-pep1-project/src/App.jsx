import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
import Home from './components/Home';
import Navbar from "./components/Navbar";
import AddEditRegister from './components/AddEditRegister';
import FinalizarReparacion from './components/FinalizarReparación';
import RetirarAuto from './components/RetirarAuto';
import Reporte1 from './components/Reporte1';
import Reporte2 from './components/Reporte2';
import Reporte3 from './components/Reporte3';
import Reporte4 from './components/Reporte4';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';

function App() {
  return (
    
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Router>
      <Navbar></Navbar>
        <div className="app-container">
          <div className="content">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/home" element={<Home />} />
              <Route path="/registro" element={<AddEditRegister />} />
              <Route path="/registro/:id" element={<AddEditRegister />} />
              <Route path="/finalizar" element={<FinalizarReparacion />} />
              <Route path="/retirar" element={<RetirarAuto />} />
              <Route path="/reporte1" element={<Reporte1 />} />
              <Route path="/reporte2" element={<Reporte2 />} />
              <Route path="/reporte3" element={<Reporte3 />} />
              <Route path="/reporte4" element={<Reporte4 />} />
            </Routes>
          </div>
        </div>
      </Router>
    </LocalizationProvider>
  );
}

export default App;
