
import { Link } from 'react-router-dom'
import './Navbar.css'

function Navbar({ isAuthenticated, currentUser, onLogout }) {
    return (
        <header className="navbar">
            <Link to="/" className="navbar-brand">
                Smart Booking System
            </Link>

            <nav className="navbar-links">
                <Link to="/">Home</Link>
                <Link to="/rooms">Rooms</Link>

                {isAuthenticated ? (
                    <>
                        <Link to="/reservations">My Reservations</Link>

                        {currentUser?.role === 'ADMIN' && (
                            <Link to="/admin">Admin Dashboard</Link>
                        )}

                        <button
                            className="navbar-logout"
                            onClick={onLogout}
                        >
                            Logout
                        </button>
                    </>
                ) : (
                    <>
                        <Link to="/login">Login</Link>
                        <Link to="/register" className="navbar-register">
                            Register
                        </Link>
                    </>
                )}
            </nav>
        </header>
    )
}

export default Navbar
