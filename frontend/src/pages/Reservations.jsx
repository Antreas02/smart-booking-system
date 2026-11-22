
import { useEffect, useState } from 'react'
import { apiRequest } from '../services/api.js'
import './Reservations.css'

function Reservations() {
    const [reservations, setReservations] = useState([])
    const [rooms, setRooms] = useState([])
    const [selectedRoomId, setSelectedRoomId] = useState('')
    const [startTime, setStartTime] = useState('')
    const [endTime, setEndTime] = useState('')
    const [selectedStatus, setSelectedStatus] = useState('ALL')
    const [message, setMessage] = useState('Loading reservations...')
    const [toast, setToast] = useState(null)

    // Automatically hide the notification after 4 seconds
    useEffect(() => {
        if (!toast) {
            return
        }

        const timer = setTimeout(() => {
            setToast(null)
        }, 4000)

        return () => clearTimeout(timer)
    }, [toast])

    useEffect(() => {
        async function fetchData() {
            try {
                const reservationsResponse = await apiRequest('/reservations')
                const reservationsData = await reservationsResponse.json()

                const roomsResponse = await apiRequest('/rooms')
                const roomsData = await roomsResponse.json()

                setReservations(reservationsData)
                setRooms(roomsData)
                setMessage('')
            } catch (error) {
                setMessage(error.message)
            }
        }

        fetchData()
    }, [])

    async function handleCreateReservation(event) {
        event.preventDefault()
        setToast(null)

        const reservationData = {
            roomId: Number(selectedRoomId),
            startTime: startTime,
            endTime: endTime,
            status: 'CONFIRMED'
        }

        try {
            const response = await apiRequest('/reservations', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(reservationData)
            })

            const createdReservation = await response.json()

            setReservations((previousReservations) => [
                ...previousReservations,
                createdReservation
            ])

            setSelectedRoomId('')
            setStartTime('')
            setEndTime('')

            setToast({
                type: 'success',
                message: 'Reservation created successfully!'
            })
        } catch (error) {
            setToast({
                type: 'error',
                message: error.message
            })
        }
    }

    async function handleCancelReservation(reservationId) {
        const confirmed = window.confirm(
            'Are you sure you want to cancel this reservation?'
        )

        if (!confirmed) {
            return
        }

        setToast(null)

        try {
            const response = await apiRequest(`/reservations/${reservationId}/cancel`, {
                method: 'PATCH'
            })

            const cancelledReservation = await response.json()

            setReservations((previousReservations) =>
                previousReservations.map((reservation) =>
                    reservation.id === cancelledReservation.id
                        ? cancelledReservation
                        : reservation
                )
            )

            setToast({
                type: 'success',
                message: 'Reservation cancelled successfully!'
            })
        } catch (error) {
            setToast({
                type: 'error',
                message: error.message
            })
        }
    }

    const filteredReservations = reservations.filter((reservation) => {
        if (selectedStatus === 'ALL') {
            return true
        }

        return reservation.status === selectedStatus
    })

    const currentDateTime = new Date().toISOString().slice(0, 16)

    return (
        <main className="reservations-page">
            <h2>My Reservations</h2>

            {message && <p>{message}</p>}

            {toast && (
                <div
                    className={`toast-notification ${toast.type}`}
                    role="status"
                    aria-live="polite"
                >
                    {toast.type === 'success' && (
                        <div className="toast-icon">
                            ✓
                        </div>
                    )}

                    {toast.type === 'error' && (
                        <div className="toast-icon error-icon">
                            !
                        </div>
                    )}

                    <div className="toast-content">
                        <p className="toast-message">{toast.message}</p>
                    </div>
                </div>
            )}

            <h3>Create a Reservation</h3>

            <form className="reservation-form" onSubmit={handleCreateReservation}>
                <div>
                    <label htmlFor="room">Select Room:</label>

                    <select
                        id="room"
                        value={selectedRoomId}
                        onChange={(event) => setSelectedRoomId(event.target.value)}
                        required
                    >
                        <option value="">Choose a room</option>

                        {rooms
                            .filter((room) => room.available)
                            .map((room) => (
                                <option key={room.id} value={room.id}>
                                    {room.name}
                                </option>
                            ))}
                    </select>
                </div>

                <div>
                    <label htmlFor="startTime">Start Time:</label>
                    <input
                        type="datetime-local"
                        id="startTime"
                        value={startTime}
                        onChange={(event) => setStartTime(event.target.value)}
                        min={currentDateTime}
                        required
                    />
                </div>

                <div>
                    <label htmlFor="endTime">End Time:</label>
                    <input
                        type="datetime-local"
                        id="endTime"
                        value={endTime}
                        onChange={(event) => setEndTime(event.target.value)}
                        min={startTime ? new Date(new Date(startTime).getTime() + 60000).toISOString().slice(0, 16) : currentDateTime}
                        required
                    />
                </div>

                <button type="submit">Create Reservation</button>
            </form>

            <h3>All Reservations</h3>

            <div className="reservation-filter">
                <label htmlFor="statusFilter">Filter by status:</label>

                <select
                    id="statusFilter"
                    value={selectedStatus}
                    onChange={(event) => setSelectedStatus(event.target.value)}
                >
                    <option value="ALL">All Reservations</option>
                    <option value="CONFIRMED">Confirmed</option>
                    <option value="CANCELLED">Cancelled</option>
                </select>
            </div>

            {filteredReservations.length === 0 ? (
                <p>No reservations found.</p>
            ) : (
                <div className="reservations-list">
                    {filteredReservations.map((reservation) => (
                        <div className="reservation-card" key={reservation.id}>
                            <h3>{reservation.room.name}</h3>

                            <p>Reservation ID: {reservation.id}</p>
                            <p>Location: {reservation.room.location}</p>
                            <p>
                                Start: {new Date(reservation.startTime).toLocaleString('en-GB')}
                            </p>
                            <p>
                                End: {new Date(reservation.endTime).toLocaleString('en-GB')}
                            </p>

                            <p>
                                Status:{' '}
                                <span className={`reservation-status ${reservation.status.toLowerCase()}`}>
                                    {reservation.status}
                                </span>
                            </p>

                            {reservation.status === 'CONFIRMED' && (
                                <button
                                    onClick={() => handleCancelReservation(reservation.id)}
                                >
                                    Cancel Reservation
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </main>
    )
}

export default Reservations
