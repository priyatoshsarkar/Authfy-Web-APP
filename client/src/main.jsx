import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import App from './App.jsx';
import './index.css';
import 'bootstrap/dist/js/bootstrap.bundle.js';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { AppContextProvider } from './context/AppContext.jsx';
ReactDOM.createRoot(document.getElementById('root')).render(
  
    <BrowserRouter>
      <AppContextProvider>
         <App />
      </AppContextProvider>
    </BrowserRouter>
);
