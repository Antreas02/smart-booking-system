
const API_URL = 'http://localhost:8080'

export async function apiRequest(endpoint, options = {}) {
    const token = localStorage.getItem('token')

    const headers = {
        ...options.headers
    }

    if (token) {
        headers.Authorization = `Bearer ${token}`
    }

    const response = await fetch(`${API_URL}${endpoint}`, {
        ...options,
        headers: headers
    })

    if (response.ok === false) {
        if (response.status === 401) {
            throw new Error(
                endpoint === '/users/login'
                    ? 'Invalid email or password.'
                    : 'Authentication failed. Please log in again.'
            )
        }

        if (response.status === 403) {
            throw new Error('You do not have permission to perform this action.')
        }

        if (
            response.status === 409 &&
            options.method === 'DELETE' &&
            endpoint.startsWith('/rooms/')
        ) {
            throw new Error(
                'Cannot delete this room because it has existing reservations.'
            )
        }

        let errorMessage = `Request failed with status ${response.status}.`

        try {
            const errorData = await response.json()

            errorMessage =
                errorData.message ||
                errorData.detail ||
                errorMessage
        } catch {
            // Keep the default error message if the response is not JSON.
        }

        throw new Error(errorMessage)
    }

    return response
}
