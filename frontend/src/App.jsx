
import { useState, useEffect } from 'react'
import { BrowserRouter, Routes, Route } from 'react-router-dom'

import Register from './pages/Register.jsx'
import Navbar from './components/Navbar.jsx'
import Footer from './components/Footer.jsx'
import Home from './pages/Home.jsx'
import Login from './pages/Login.jsx'
import Rooms from './pages/Rooms.jsx'
import Reservations from './pages/Reservations.jsx'
import NotFound from './pages/NotFound.jsx'
import AdminDashboard from './pages/AdminDashboard.jsx'
import AdminRoute from './components/AdminRoute.jsx'
import { apiRequest } from './services/api.js'
import './App.css'

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(
    () => Boolean(localStorage.getItem('token'))
  )

  const [currentUser, setCurrentUser] = useState(null)

  const [isLoadingUser, setIsLoadingUser] = useState(
    () => Boolean(localStorage.getItem('token'))
  )

  async function fetchCurrentUser() {
    setIsLoadingUser(true)

    try {
      const response = await apiRequest('/users/me')
      const userData = await response.json()

      setCurrentUser(userData)
      setIsAuthenticated(true)
    } catch (error) {
      localStorage.removeItem('token')
      setCurrentUser(null)
      setIsAuthenticated(false)

      console.error(error.message)
    } finally {
      setIsLoadingUser(false)
    }
  }

  useEffect(() => {
    if (localStorage.getItem('token')) {
      fetchCurrentUser()
    }
  }, [])

  async function handleLogin() {
    await fetchCurrentUser()
  }

  function handleLogout() {
    localStorage.removeItem('token')
    setCurrentUser(null)
    setIsAuthenticated(false)
    setIsLoadingUser(false)
  }

  return (
    <BrowserRouter>
      <div className="app-container">
        <Navbar
          isAuthenticated={isAuthenticated}
          currentUser={currentUser}
          onLogout={handleLogout}
        />

        <div className="page-container">
          <Routes>
            <Route
              path="/"
              element={<Home isAuthenticated={isAuthenticated} />}
            />
            <Route
              path="/login"
              element={<Login onLogin={handleLogin} />}
            />

            <Route path="/register" element={<Register />} />

            <Route
              path="/rooms"
              element={<Rooms currentUser={currentUser} />}
            />

            <Route path="/reservations" element={<Reservations />} />

            <Route
              path="/admin"
              element={
                <AdminRoute
                  currentUser={currentUser}
                  isLoadingUser={isLoadingUser}
                >
                  <AdminDashboard />
                </AdminRoute>
              }
            />

            <Route path="*" element={<NotFound />} />
          </Routes>
        </div>

        <Footer />
      </div>
    </BrowserRouter>
  )
}

export default App
