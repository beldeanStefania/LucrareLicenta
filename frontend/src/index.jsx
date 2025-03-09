import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import './styles/designSystem.css';
import App from './components/App';
import 'bootstrap/dist/css/bootstrap.min.css';

// Add Google Fonts
const googleFonts = document.createElement('link');
googleFonts.rel = 'stylesheet';
googleFonts.href = 'https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&family=Roboto+Slab:wght@400;700&display=swap';
document.head.appendChild(googleFonts);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <App />
);
