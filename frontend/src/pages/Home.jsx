
import { Link } from 'react-router-dom'
import './Home.css'

function Home({ isAuthenticated }) {
    return (
        <main className="home-page">
            <h2>Welcome to Smart Booking System</h2>

            <p>
                Easily explore available rooms, create reservations
                and manage your bookings through one simple platform.
            </p>

            <div className="home-navigation">
                <Link to="/rooms">Explore Rooms</Link>

                {!isAuthenticated && (
                    <Link to="/register" className="home-secondary-link">
                        Create an Account
                    </Link>
                )}
            </div>
        </main>
    )
}

export default Home
