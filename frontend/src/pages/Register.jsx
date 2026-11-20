
import { useState } from 'react'
import { apiRequest } from '../services/api.js'
import './Login.css'

function Register() {
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    async function handleRegister(event) {
        event.preventDefault()

        const userData = {
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password
        }

        try {
            const response = await apiRequest('/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            })

            const data = await response.json()

            console.log(data)
        } catch (error) {
            console.error(error.message)
        }
    }

    return (
        <main className="auth-page register-page">
            <h2>Register</h2>

            <p className="auth-message">
                Create a new account to get started.
            </p>

            <form className="auth-form" onSubmit={handleRegister}>
                <div>
                    <label htmlFor="firstName">First Name:</label>
                    <input
                        type="text"
                        id="firstName"
                        placeholder="Enter your first name"
                        value={firstName}
                        onChange={(event) => setFirstName(event.target.value)}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="lastName">Last Name:</label>
                    <input
                        type="text"
                        id="lastName"
                        placeholder="Enter your last name"
                        value={lastName}
                        onChange={(event) => setLastName(event.target.value)}
                        required
                    />
                </div>

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

                <button type="submit">Register</button>
            </form>
        </main>
    )
}

export default Register
