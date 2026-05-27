// Header.jsx - DentalCare+ Header
import { useState, useEffect } from 'react'
import { Link, useLocation } from 'react-router-dom'
import { FaBars, FaTimes, FaSearch, FaList, FaTooth } from 'react-icons/fa'

const Header = ({ openAuthModal }) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false)
  const [isScrolled, setIsScrolled] = useState(false)
  const location = useLocation()

  // Handle scroll effect
  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20)
    }
    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])

  const navLinks = [
    { path: '/', label: 'Home' },
    { path: '/browse-dispensaries', label: 'Browse Clinics', icon: FaList },
    { path: '/find-dispensaries', label: 'Find Nearby', icon: FaSearch }
  ]

  const handleMobileMenuClick = () => {
    setIsMenuOpen(false)
  }

  const handleAuthClick = () => {
    openAuthModal()
    setIsMenuOpen(false)
  }

  return (
    <header
      className={`sticky top-0 z-40 transition-all duration-300 ${isScrolled
          ? 'bg-white/97 backdrop-blur-md shadow-card'
          : 'bg-white/85 backdrop-blur-sm'
        }`}
    >
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center h-18 py-4">
          {/* Logo */}
          <Link
            to="/"
            className="flex items-center gap-2 group"
          >
            <div className="relative">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-primary-400 to-secondary-500 flex items-center justify-center shadow-glow transition-transform duration-300 group-hover:scale-110">
                <FaTooth className="text-white text-xl" />
              </div>
              <div className="absolute -top-1 -right-1 w-3 h-3 bg-secondary-400 rounded-full animate-pulse-gentle"></div>
            </div>
            <span className="text-2xl font-display font-bold logo-text">
              Dental<span className="text-primary-500">Care+</span>
            </span>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex items-center space-x-2">
            {navLinks.map(({ path, label, icon: Icon }) => (
              <Link
                key={path}
                to={path}
                className={`nav-link flex items-center px-4 py-2 text-sm font-medium rounded-lg transition-all duration-200 ${location.pathname === path
                    ? 'text-primary-600 bg-primary-50'
                    : 'text-neutral-600 hover:text-primary-600 hover:bg-primary-50/50'
                  }`}
              >
                {Icon && <Icon className="mr-2 text-sm" />}
                {label}
              </Link>
            ))}

            <div className="w-px h-6 bg-neutral-200 mx-2"></div>

            <button
              onClick={openAuthModal}
              className="btn-primary text-sm"
            >
              <span className="relative z-10">Book Appointment</span>
            </button>
          </nav>

          {/* Mobile Menu Button */}
          <button
            className="md:hidden p-3 rounded-xl text-neutral-600 hover:text-primary-600 hover:bg-primary-50 focus:outline-none focus:ring-2 focus:ring-primary-400 transition-all duration-200"
            onClick={() => setIsMenuOpen(!isMenuOpen)}
            aria-label="Toggle menu"
          >
            {isMenuOpen ? <FaTimes size={22} /> : <FaBars size={22} />}
          </button>
        </div>

        {/* Mobile Menu */}
        <div
          className={`md:hidden overflow-hidden transition-all duration-300 ${isMenuOpen ? 'max-h-96 pb-6' : 'max-h-0'
            }`}
        >
          <nav className="pt-4 border-t border-neutral-100">
            <div className="space-y-2">
              {navLinks.map(({ path, label, icon: Icon }, index) => (
                <Link
                  key={path}
                  to={path}
                  onClick={handleMobileMenuClick}
                  className={`flex items-center px-4 py-3 text-base font-medium rounded-xl transition-all duration-200 stagger-item animate-fade-in-up ${location.pathname === path
                      ? 'bg-primary-50 text-primary-600'
                      : 'text-neutral-600 hover:bg-neutral-50 hover:text-primary-600'
                    }`}
                  style={{ animationDelay: `${index * 0.1}s` }}
                >
                  {Icon && <Icon className="mr-3 text-lg" />}
                  {label}
                </Link>
              ))}

              <div className="pt-4">
                <button
                  onClick={handleAuthClick}
                  className="w-full btn-primary text-base py-3"
                >
                  Book Appointment
                </button>
              </div>
            </div>
          </nav>
        </div>
      </div>
    </header>
  )
}

export default Header
