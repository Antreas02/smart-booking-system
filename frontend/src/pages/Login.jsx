
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { apiRequest } from '../services/api.js'
import './Login.css'

function Login({ onLogin }) {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [message, setMessage] = useState('')

    const navigate = useNavigate()

    async function handleLogin(event) {
        event.preventDefault()

        setMessage('')

        const loginData = {
            email: email,
            password: password
        }

        try {
            const response = await apiRequest('/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData)
            })

            const data = await response.json()

            localStorage.setItem('token', data.token)

            await onLogin()

            navigate('/')
        } catch (error) {
            setMessage(error.message)
        }
    }

    return (
        <main className="auth-page">
            <h2>Login</h2>

            <form className="auth-form" onSubmit={handleLogin}>
                <div>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        placeholder="Enter your password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                        required
                        minLength={6}
                    />
                </div>

                <button type="submit">Login</button>
            </form>

            {message && <p className="auth-message">{message}</p>}
        </main>
    )
}

export default Login
