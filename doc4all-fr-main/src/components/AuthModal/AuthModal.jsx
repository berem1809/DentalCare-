// AuthModal.jsx - Premium Authentication Modal
import { useState } from 'react'
import { FaHeartbeat, FaTimes } from 'react-icons/fa'
import LoginForm from './LoginForm'
import RegistrationForm from './RegistrationForm'

const AuthModal = ({ onClose }) => {
  const [view, setView] = useState('login')

  const showLogin = () => setView('login')
  const showRegister = () => setView('register')

  // Handle backdrop click to close modal
  const handleBackdropClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose()
    }
  }

  return (
    <div
      className="modal-backdrop fixed inset-0 flex items-center justify-center z-50 p-4 animate-fade-in"
      onClick={handleBackdropClick}
    >
      <div className="modal-content bg-white rounded-3xl shadow-2xl w-full max-w-md max-h-[90vh] flex flex-col relative animate-scale-in overflow-hidden">
        {/* Decorative top gradient */}
        <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-primary-500 via-secondary-500 to-accent-500"></div>

        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 w-10 h-10 flex items-center justify-center rounded-full bg-neutral-100 hover:bg-neutral-200 text-neutral-500 hover:text-neutral-700 transition-all duration-200 z-10 hover:rotate-90"
          aria-label="Close modal"
        >
          <FaTimes />
        </button>

        {/* Header with logo */}
        <div className="flex-shrink-0 px-8 pt-8 pb-4">
          {/* Logo */}
          <div className="flex items-center justify-center gap-2 mb-6">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-primary-500 to-secondary-500 flex items-center justify-center shadow-glow">
              <FaHeartbeat className="text-white text-xl" />
            </div>
            <span className="text-2xl font-display font-bold logo-text">
              MediCare<span className="text-primary-500">+</span>
            </span>
          </div>

          {/* Tabs */}
          <div className="flex justify-center">
            <div className="relative flex bg-neutral-100 rounded-xl p-1">
              {/* Sliding background */}
              <div
                className={`absolute top-1 bottom-1 w-[calc(50%-4px)] rounded-lg bg-white shadow-sm transition-transform duration-300 ${view === 'register' ? 'translate-x-full' : 'translate-x-0'
                  }`}
              ></div>

              <button
                onClick={showLogin}
                className={`relative z-10 px-8 py-2.5 text-sm font-semibold rounded-lg transition-colors duration-200 ${view === 'login'
                    ? 'text-primary-600'
                    : 'text-neutral-500 hover:text-neutral-700'
                  }`}
              >
                Log In
              </button>
              <button
                onClick={showRegister}
                className={`relative z-10 px-8 py-2.5 text-sm font-semibold rounded-lg transition-colors duration-200 ${view === 'register'
                    ? 'text-primary-600'
                    : 'text-neutral-500 hover:text-neutral-700'
                  }`}
              >
                Sign Up
              </button>
            </div>
          </div>
        </div>

        {/* Scrollable content area */}
        <div className="flex-1 overflow-y-auto px-8 pb-8">
          <div className={`transition-all duration-300 ${view === 'login' ? 'animate-slide-in-left' : 'animate-slide-in-right'}`}>
            {view === 'login' ? (
              <LoginForm switchToRegister={showRegister} closeModal={onClose} />
            ) : (
              <RegistrationForm switchToLogin={showLogin} closeModal={onClose} />
            )}
          </div>
        </div>

        {/* Bottom decoration */}
        <div className="absolute bottom-0 left-0 right-0 h-24 bg-gradient-to-t from-neutral-50/80 to-transparent pointer-events-none"></div>
      </div>
    </div>
  )
}

export default AuthModal