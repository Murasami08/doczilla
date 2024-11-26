const express = require('express');
const cors = require('cors');
const axios = require('axios');
const app = express();
const PORT = process.env.PORT || 3000;
app.use(cors());
app.get('/api/todos', async (req, res) => {
    try {
        const response = await axios.get('https://todo.doczilla.pro/api/todos');
        res.json(response.data);
    } catch (error) {
        res.status(500).send(error.message);
    }
});
app.get('/api/todos/date', async (req, res) => {
    const { from, to, status } = req.query;
    try {
        const response = await axios.get(`https://todo.doczilla.pro/api/todos/date?from=${from}&to=${to}&status=${status}`);
        res.json(response.data);
    } catch (error) {
        res.status(500).send(error.message);
    }
});
app.get('/api/todos/find', async (req, res) => {
    const { q } = req.query;
    try {
        const response = await axios.get(`https://todo.doczilla.pro/api/todos/find?q=${q}`);
        res.json(response.data);
    } catch (error) {
        res.status(500).send(error.message);
    }
});
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});

