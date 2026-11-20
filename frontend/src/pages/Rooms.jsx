
import { useEffect, useState } from 'react'
import { apiRequest } from '../services/api.js'
import './Rooms.css'

function Rooms({ currentUser }) {
    const isAdmin = currentUser?.role === 'ADMIN'

    const [rooms, setRooms] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState('')
    const [message, setMessage] = useState('')

    const [name, setName] = useState('')
    const [capacity, setCapacity] = useState('')
    const [location, setLocation] = useState('')
    const [available, setAvailable] = useState(false)

    const [editingRoom, setEditingRoom] = useState(null)

    useEffect(() => {
        async function fetchRooms() {
            try {
                const response = await apiRequest('/rooms')
                const data = await response.json()

                setRooms(data)
            } catch (error) {
                setError(error.message)
            } finally {
                setLoading(false)
            }
        }

        fetchRooms()
    }, [])

    async function handleCreateRoom(event) {
        event.preventDefault()
        setMessage('')

        const newRoom = {
            name: name,
            capacity: Number(capacity),
            location: location,
            available: available
        }

        try {
            const response = await apiRequest('/rooms', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newRoom)
            })

            const createdRoom = await response.json()

            setRooms((previousRooms) => [...previousRooms, createdRoom])

            setName('')
            setCapacity('')
            setLocation('')
            setAvailable(false)

            setMessage('Room created successfully!')
        } catch (error) {
            setMessage(error.message)
        }
    }

    function handleEditClick(room) {
        setEditingRoom({
            id: room.id,
            name: room.name,
            capacity: room.capacity,
            location: room.location,
            available: room.available
        })

        setMessage('')
    }

    async function handleUpdateRoom(event) {
        event.preventDefault()
        setMessage('')

        const updatedRoom = {
            name: editingRoom.name,
            capacity: Number(editingRoom.capacity),
            location: editingRoom.location,
            available: editingRoom.available
        }

        try {
            const response = await apiRequest(`/rooms/${editingRoom.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedRoom)
            })

            const savedRoom = await response.json()

            setRooms((previousRooms) =>
                previousRooms.map((room) =>
                    room.id === savedRoom.id ? savedRoom : room
                )
            )

            setEditingRoom(null)
            setMessage('Room updated successfully!')
        } catch (error) {
            setMessage(error.message)
        }
    }

    async function handleDeleteRoom(roomId) {
        const confirmed = window.confirm(
            'Are you sure you want to delete this room?'
        )

        if (!confirmed) {
            return
        }

        setMessage('')

        try {
            await apiRequest(`/rooms/${roomId}`, {
                method: 'DELETE'
            })

            setRooms((previousRooms) =>
                previousRooms.filter((room) => room.id !== roomId)
            )

            setMessage('Room deleted successfully!')
        } catch (error) {
            setMessage(error.message)
        }
    }

    if (loading) {
        return <main><p>Loading rooms...</p></main>
    }

    if (error) {
        return <main><p>{error}</p></main>
    }

    return (
        <main className="rooms-page">
            <h2>Available Rooms</h2>

            {message && <p>{message}</p>}

            {isAdmin && (
                <>
                    <h3>Create a Room</h3>

                    <form className="room-form" onSubmit={handleCreateRoom}>
                        <div>
                            <label htmlFor="name">Room Name:</label>
                            <input
                                type="text"
                                id="name"
                                value={name}
                                onChange={(event) => setName(event.target.value)}
                                required
                            />
                        </div>

                        <div>
                            <label htmlFor="capacity">Capacity:</label>
                            <input
                                type="number"
                                id="capacity"
                                value={capacity}
                                onChange={(event) => setCapacity(event.target.value)}
                                min="1"
                                required
                            />
                        </div>

                        <div>
                            <label htmlFor="location">Location:</label>
                            <input
                                type="text"
                                id="location"
                                value={location}
                                onChange={(event) => setLocation(event.target.value)}
                                required
                            />
                        </div>

                        <div>
                            <label htmlFor="available">Available:</label>
                            <input
                                type="checkbox"
                                id="available"
                                checked={available}
                                onChange={(event) => setAvailable(event.target.checked)}
                            />
                        </div>

                        <button type="submit">Create Room</button>
                    </form>
                </>
            )}

            <h3>All Rooms</h3>

            {rooms.length === 0 ? (
                <p>No rooms found.</p>
            ) : (
                <div className="rooms-grid">
                    {rooms.map((room) => (
                        <div className="room-card" key={room.id}>
                            <h3>{room.name}</h3>

                            <p>Capacity: {room.capacity}</p>
                            <p>Location: {room.location}</p>

                            <span
                                className={`room-status ${room.available ? 'available' : 'unavailable'
                                    }`}
                            >
                                {room.available ? 'Available' : 'Unavailable'}
                            </span>

                            {isAdmin && (
                                <>
                                    <div className="room-actions">
                                        <button onClick={() => handleEditClick(room)}>
                                            Edit
                                        </button>

                                        <button onClick={() => handleDeleteRoom(room.id)}>
                                            Delete
                                        </button>
                                    </div>

                                    {editingRoom && editingRoom.id === room.id && (
                                        <form onSubmit={handleUpdateRoom}>
                                            <h4>Edit Room</h4>

                                            <div>
                                                <label htmlFor="editName">Room Name:</label>
                                                <input
                                                    type="text"
                                                    id="editName"
                                                    value={editingRoom.name}
                                                    onChange={(event) =>
                                                        setEditingRoom({
                                                            ...editingRoom,
                                                            name: event.target.value
                                                        })
                                                    }
                                                    required
                                                />
                                            </div>

                                            <div>
                                                <label htmlFor="editCapacity">Capacity:</label>
                                                <input
                                                    type="number"
                                                    id="editCapacity"
                                                    value={editingRoom.capacity}
                                                    onChange={(event) =>
                                                        setEditingRoom({
                                                            ...editingRoom,
                                                            capacity: event.target.value
                                                        })
                                                    }
                                                    min="1"
                                                    required
                                                />
                                            </div>

                                            <div>
                                                <label htmlFor="editLocation">Location:</label>
                                                <input
                                                    type="text"
                                                    id="editLocation"
                                                    value={editingRoom.location}
                                                    onChange={(event) =>
                                                        setEditingRoom({
                                                            ...editingRoom,
                                                            location: event.target.value
                                                        })
                                                    }
                                                    required
                                                />
                                            </div>

                                            <div>
                                                <label htmlFor="editAvailable">Available:</label>
                                                <input
                                                    type="checkbox"
                                                    id="editAvailable"
                                                    checked={editingRoom.available}
                                                    onChange={(event) =>
                                                        setEditingRoom({
                                                            ...editingRoom,
                                                            available: event.target.checked
                                                        })
                                                    }
                                                />
                                            </div>

                                            <button type="submit">Save Changes</button>

                                            <button
                                                type="button"
                                                onClick={() => setEditingRoom(null)}
                                            >
                                                Cancel
                                            </button>
                                        </form>
                                    )}
                                </>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </main>
    )
}

export default Rooms
