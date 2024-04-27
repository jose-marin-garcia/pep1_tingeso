import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
import Home from './components/Home';
import Navbar from "./components/Navbar";
import AddEditRegister from './components/AddEditRegister';
import Reporte1 from './components/Reporte1';
import Reporte2 from './components/Reporte2';
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
              <Route path="/reporte1" element={<Reporte1 />} />
              <Route path="/reporte2" element={<Reporte2 />} />
            </Routes>
          </div>
        </div>
      </Router>
    </LocalizationProvider>
  );
}

export default App;
