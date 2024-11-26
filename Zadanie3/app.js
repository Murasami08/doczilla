const apiUrl = 'http://localhost:3000/api/todos';
const taskListDiv = document.getElementById('taskList');
const modal = document.getElementById('Modal');
const taskTitle = document.getElementById('modalTitle');
const taskDescription = document.getElementById('modalDescription');
const closeModal = document.getElementById('closeModal');
const todayBtn = document.getElementById('todayBtn');
const weekBtn = document.getElementById('weekBtn');

async function fetchTodos() {
    try {
        const response = await fetch(apiUrl);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return Array.isArray(data) ? data : []; 
    } catch (error) {
        console.error('Ошибка при загрузке задач:', error);
        return []; 
    }
}

function displayTodos(todos) {
    if (!Array.isArray(todos)) {
        console.error('Expected an array of todos, but got:', todos);
        return; 
    }

    taskListDiv.innerHTML = '';
    todos.forEach(todo => {
        const taskDiv = document.createElement('p');
        taskDiv.style.cursor = 'pointer';
        taskDiv.innerHTML = `<strong>${todo.name}</strong>  - ${todo.status ? '<span style="color:green;">Выполнено</span>' : '<span style="color:red;">Не Выполнено</span>'}`;
        taskDiv.addEventListener('click', () => {
            modal.style.display = 'block';
            taskTitle.textContent = todo.name;
            taskDescription.innerHTML = todo.shortDesc + ' ' + todo.date;

        });
        taskListDiv.appendChild(taskDiv);
    });
}
document.querySelector('.close').onclick = () => {
    document.getElementById('Modal').style.display = "none";
};

function getCurrentDate() {
    const date = new Date();
    return date.toISOString().split('T')[0];
}

todayBtn.addEventListener('click', async() => {
    const today = getCurrentDate();
    let res = await fetch(`${apiUrl}/date?from=${today}&to=${today}`);
    const todos = await res.text();
    displayTodos(todos);
})
function getStartOfWeek() {
    const date = new Date();
    const day = date.getUTCDay() || 7; 
    if (day !== 1) date.setUTCDate(date.getUTCDate() - (day - 1)); 
    return date.toISOString().split('T')[0];
}
function getEndOfWeek() {
    const date = new Date();
    const day = date.getUTCDay() || 7; 
    if (day !== 0) date.setUTCDate(date.getUTCDate() + (7 - day)); 
    return date.toISOString().split('T')[0];
}

weekBtn.addEventListener('click', async() => {
    const startDate = getStartOfWeek();
    const endDate = getEndOfWeek();
    let res = await fetch(`${apiUrl}/date?from=${startDate}&to=${endDate}`);
    const todos = await res.text();
    displayTodos(todos);
})

async function searchTasks() {
    const query = document.getElementById('search').value;
    const todos = await fetchTodos();
    const filteredTodos = todos.filter(todo => todo.name.toLowerCase().includes(query.toLowerCase()));
    displayTodos(filteredTodos);
}


async function filterByDate() {
    const startDate = new Date(document.getElementById('startDate').value);
    const endDate = new Date(document.getElementById('endDate').value);
    const todos = await fetchTodos();
    
    const filteredTodos = todos.filter(todo => {
        const todoDate = new Date(todo.date); 
        return todoDate >= startDate && todoDate <= endDate;
    });

    displayTodos(filteredTodos);
}

async function filterByStatus() {
    const status = document.getElementById('statusFilter').value;
    const todos = await fetchTodos();
    let filteredTodos;
    if (status === 'completed') {
        filteredTodos = todos.filter(todo => todo.status);
    } else if (status === 'not_completed') {
        filteredTodos = todos.filter(todo => !todo.status);
    } else {
        filteredTodos = todos; 
    }

    displayTodos(filteredTodos);
}



document.getElementById('searchBtn').onclick = searchTasks;

    document.getElementById('filterByDateBtn').onclick = filterByDate;
    document.getElementById('statusFilter').onchange = filterByStatus;

    fetchTodos().then(displayTodos);
