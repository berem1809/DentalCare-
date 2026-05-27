// LoginForm.jsx - Premium Login Form
import { useState } from "react";
import { FaEnvelope, FaLock, FaSpinner, FaGoogle, FaApple } from 'react-icons/fa';

const LoginForm = ({ switchToRegister, closeModal }) => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    rememberMe: false
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value
    })
    // Clear error when user types
    if (error) setError('')
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const res = await fetch('http://localhost:8005/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password
        })
      })

      if (!res.ok) {
        const text = await res.text()
        throw new Error(text || 'Invalid credentials')
      }

      const data = await res.json()
      console.log('Logged in:', data)
      closeModal()
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="py-4">
      {/* Welcome text */}
      <div className="text-center mb-8">
        <h2 className="text-2xl font-display font-bold text-neutral-900 mb-2">Welcome Back</h2>
        <p className="text-neutral-500">Sign in to continue your health journey</p>
      </div>

      {/* Error message */}
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-xl mb-6 flex items-center gap-2 animate-fade-in">
          <svg className="w-5 h-5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
          </svg>
          <span className="text-sm">{error}</span>
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-5">
        {/* Email Field */}
        <div className="glow-on-focus rounded-xl">
          <label htmlFor="loginEmail" className="form-label">
            Email Address
          </label>
          <div className="relative">
            <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
              <FaEnvelope className="text-neutral-400" />
            </div>
            <input
              type="email"
              id="loginEmail"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              placeholder="Enter your email"
              className="input-field pl-11"
              required
            />
          </div>
        </div>

        {/* Password Field */}
        <div className="glow-on-focus rounded-xl">
          <label htmlFor="loginPassword" className="form-label">
            Password
          </label>
          <div className="relative">
            <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
              <FaLock className="text-neutral-400" />
            </div>
            <input
              type="password"
              id="loginPassword"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              placeholder="Enter your password"
              className="input-field pl-11"
              required
            />
          </div>
        </div>

        {/* Remember me & Forgot password */}
        <div className="flex items-center justify-between">
          <label className="inline-flex items-center cursor-pointer group">
            <input
              type="checkbox"
              name="rememberMe"
              checked={formData.rememberMe}
              onChange={handleInputChange}
              className="w-4 h-4 rounded border-neutral-300 text-primary-600 focus:ring-primary-500 cursor-pointer"
            />
            <span className="ml-2 text-sm text-neutral-600 group-hover:text-neutral-900 transition-colors">
              Remember me
            </span>
          </label>
          <button
            type="button"
            className="text-sm text-primary-600 hover:text-primary-700 font-medium animated-underline"
          >
            Forgot Password?
          </button>
        </div>

        {/* Submit Button */}
        <button
          type="submit"
          className="w-full btn-primary py-3.5 flex items-center justify-center gap-2"
          disabled={loading}
        >
          {loading ? (
            <>
              <FaSpinner className="animate-spin" />
              <span>Signing In...</span>
            </>
          ) : (
            'Sign In'
          )}
        </button>
      </form>

      {/* Divider */}
      <div className="relative my-8">
        <div className="absolute inset-0 flex items-center">
          <div className="w-full border-t border-neutral-200"></div>
        </div>
        <div className="relative flex justify-center text-sm">
          <span className="px-4 bg-white text-neutral-500">Or continue with</span>
        </div>
      </div>

      {/* Social Login Buttons */}
      <div className="grid grid-cols-2 gap-4">
        <button
          type="button"
          className="flex items-center justify-center gap-2 py-3 px-4 rounded-xl border border-neutral-200 bg-white hover:bg-neutral-50 transition-all duration-200 hover:shadow-card group"
        >
          <FaGoogle className="text-red-500 group-hover:scale-110 transition-transform" />
          <span className="text-sm font-medium text-neutral-700">Google</span>
        </button>
        <button
          type="button"
          className="flex items-center justify-center gap-2 py-3 px-4 rounded-xl border border-neutral-200 bg-white hover:bg-neutral-50 transition-all duration-200 hover:shadow-card group"
        >
          <FaApple className="text-neutral-900 group-hover:scale-110 transition-transform" />
          <span className="text-sm font-medium text-neutral-700">Apple</span>
        </button>
      </div>

      {/* Sign up link */}
      <p className="text-center text-sm text-neutral-600 mt-8">
        Don't have an account?{' '}
        <button
          type="button"
          onClick={switchToRegister}
          className="text-primary-600 hover:text-primary-700 font-semibold animated-underline"
        >
          Create Account
        </button>
      </p>
    </div>
  )
}

export default LoginForm