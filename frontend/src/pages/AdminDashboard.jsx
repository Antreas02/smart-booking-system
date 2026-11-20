
import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { apiRequest } from '../services/api.js'
import './AdminDashboard.css'

function AdminDashboard() {
    const [users, setUsers] = useState([])
    const [rooms, setRooms] = useState([])
    const [reservations, setReservations] = useState([])
    const [message, setMessage] = useState('Loading dashboard...')

    useEffect(() => {
        async function fetchDashboardData() {
            try {
                const usersResponse = await apiRequest('/users')
                const usersData = await usersResponse.json()

                const roomsResponse = await apiRequest('/rooms')
                const roomsData = await roomsResponse.json()

                const reservationsResponse = await apiRequest('/reservations')
                const reservationsData = await reservationsResponse.json()

                setUsers(usersData)
                setRooms(roomsData)
                setReservations(reservationsData)

                setMessage('')
            } catch (error) {
                setMessage(error.message)
            }
        }

        fetchDashboardData()
    }, [])

    const confirmedReservations = reservations.filter(
        (reservation) => reservation.status === 'CONFIRMED'
    )

    const cancelledReservations = reservations.filter(
        (reservation) => reservation.status === 'CANCELLED'
    )

    const availableRooms = rooms.filter(
        (room) => room.available === true
    )

    const unavailableRooms = rooms.filter(
        (room) => room.available === false
    )

    const totalReservations = reservations.length

    const confirmedPercentage = totalReservations > 0
        ? Math.round((confirmedReservations.length / totalReservations) * 100)
        : 0

    const cancelledPercentage = totalReservations > 0
        ? Math.round((cancelledReservations.length / totalReservations) * 100)
        : 0

    const recentReservations = [...reservations]
        .sort((firstReservation, secondReservation) =>
            secondReservation.id - firstReservation.id
        )
        .slice(0, 5)

    return (
        <main className="admin-dashboard">
            <h2>Admin Dashboard</h2>

            <p className="dashboard-welcome">
                Welcome to the administration panel.
            </p>

            <section className="dashboard-section">
                <h3>Quick Navigation</h3>

                <div className="dashboard-navigation">
                    <Link to="/rooms">Manage Rooms</Link>
                    <Link to="/reservations">Manage Reservations</Link>
                </div>
            </section>

            <section className="dashboard-section">
                <h3>Overview</h3>

                {message && <p>{message}</p>}

                {!message && (
                    <div className="dashboard-overview">
                        <div className="dashboard-stat-card">
                            <h4>Total Users</h4>
                            <p>{users.length}</p>
                        </div>

                        <div className="dashboard-stat-card">
                            <h4>Total Rooms</h4>
                            <p>{rooms.length}</p>
                        </div>

                        <div className="dashboard-stat-card">
                            <h4>Total Reservations</h4>
                            <p>{totalReservations}</p>
                        </div>

                        <div className="dashboard-stat-card">
                            <h4>Confirmed Reservations</h4>
                            <p>{confirmedReservations.length}</p>
                        </div>

                        <div className="dashboard-stat-card">
                            <h4>Cancelled Reservations</h4>
                            <p>{cancelledReservations.length}</p>
                        </div>
                    </div>
                )}
            </section>

            {!message && (
                <>
                    <section className="dashboard-section">
                        <h3>Room Availability</h3>

                        <div className="dashboard-card">
                            <p>Available Rooms: {availableRooms.length}</p>
                            <p>Unavailable Rooms: {unavailableRooms.length}</p>
                        </div>
                    </section>

                    <section className="dashboard-section">
                        <h3>Reservation Status</h3>

                        <div className="dashboard-card">
                            <div
                                className="dashboard-chart"
                                style={{
                                    background: `conic-gradient(
                                        #16a34a 0% ${confirmedPercentage}%,
                                        #dc2626 ${confirmedPercentage}% 100%
                                    )`
                                }}
                                role="img"
                                aria-label={`${confirmedPercentage}% confirmed and ${cancelledPercentage}% cancelled`}
                            />

                            <div className="dashboard-chart-legend">
                                <p>
                                    <span style={{ color: '#16a34a' }}>●</span>
                                    {' '}Confirmed: {confirmedReservations.length} ({confirmedPercentage}%)
                                </p>

                                <p>
                                    <span style={{ color: '#dc2626' }}>●</span>
                                    {' '}Cancelled: {cancelledReservations.length} ({cancelledPercentage}%)
                                </p>
                            </div>
                        </div>
                    </section>

                    <section className="dashboard-section">
                        <h3>Registered Users</h3>

                        <div className="dashboard-card">
                            {users.length === 0 ? (
                                <p>No registered users found.</p>
                            ) : (
                                <div className="dashboard-table-container">
                                    <table className="dashboard-table">
                                        <thead>
                                            <tr>
                                                <th>Name</th>
                                                <th>Email</th>
                                                <th>Role</th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            {users.map((user) => (
                                                <tr key={user.id}>
                                                    <td>
                                                        {user.firstName} {user.lastName}
                                                    </td>
                                                    <td>{user.email}</td>
                                                    <td>{user.role}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </div>
                            )}
                        </div>
                    </section>

                    <section className="dashboard-section">
                        <h3>Recent Reservations</h3>

                        {recentReservations.length === 0 ? (
                            <p>No reservations found.</p>
                        ) : (
                            <div className="dashboard-recent-list">
                                {recentReservations.map((reservation) => (
                                    <div
                                        className="dashboard-reservation-card"
                                        key={reservation.id}
                                    >
                                        <h4>Reservation #{reservation.id}</h4>

                                        <p>
                                            Customer: {reservation.user.firstName}{' '}
                                            {reservation.user.lastName}
                                        </p>

                                        <p>Room: {reservation.room.name}</p>

                                        <p>
                                            Start: {new Date(reservation.startTime).toLocaleString('en-GB')}
                                        </p>

                                        <p>
                                            End: {new Date(reservation.endTime).toLocaleString('en-GB')}
                                        </p>

                                        <p>Status: {reservation.status}</p>
                                    </div>
                                ))}
                            </div>
                        )}
                    </section>
                </>
            )}
        </main>
    )
}

export default AdminDashboard
