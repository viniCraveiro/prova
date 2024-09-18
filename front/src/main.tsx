import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import App from './App.tsx';
import ComputadorDetalhe from './ComputadorDetalhe.tsx';
import './index.css';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/novo" element={<ComputadorDetalhe operacao={'criando'} />} />
        <Route path="/editar/:id" element={<ComputadorDetalhe operacao={'editando'} />} />
        <Route path="/visualizar/:id" element={<ComputadorDetalhe operacao={'visualizando'} />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
);
