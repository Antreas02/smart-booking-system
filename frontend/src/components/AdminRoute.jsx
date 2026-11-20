
import { Navigate } from 'react-router-dom'

function AdminRoute({ currentUser, isLoadingUser, children }) {
    if (isLoadingUser) {
        return <p>Loading user information...</p>
    }

    if (!currentUser) {
        return <Navigate to="/login" />
    }

    if (currentUser.role !== 'ADMIN') {
        return <Navigate to="/" />
    }

    return children
}

export default AdminRoute
