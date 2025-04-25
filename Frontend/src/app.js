import React, { useState } from 'react';
import axios from 'axios';

const App = () => {
    const [city, setCity] = useState('');
    const [weather, setWeather] = useState(null);
    const [error, setError] = useState('');

    const fetchWeather = async () => {
        setError('');
        setWeather(null);

        try {
            const response = await axios.get(`http://localhost:8080/api/weather/${city}`);
            const data = response.data;
            setWeather({
                city: data.resolvedAddress,
                date: data.days[0].datetime,
                temperature: data.days[0].temp,
                description: data.days[0].description,
            });
        } catch (err) {
            setError('No se pudo obtener el clima para esa ciudad.');
        }
    };

    return (
        <div>
            <h1>Consulta el Clima</h1>
            <input
                type="text"
                placeholder="Ingresa el nombre de la ciudad"
                value={city}
                onChange={(e) => setCity(e.target.value)}
            />
            <button onClick={fetchWeather}>Obtener Clima</button>

            {error && <p>{error}</p>}

            {weather && (
                <div>
                    <h2>{weather.city}</h2>
                    <p>Fecha: {weather.date}</p>
                    <p>Temperatura: {weather.temperature}°C</p>
                    <p>Descripción: {weather.description}</p>
                </div>
            )}
        </div>
    );
};

export default App;
