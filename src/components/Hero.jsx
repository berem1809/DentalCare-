// Hero.jsx - DentalCare+ Premium Hero Section
import { FaArrowRight, FaTooth, FaUserMd, FaClinicMedical, FaShieldAlt } from 'react-icons/fa'

const Hero = ({ openAuthModal }) => {
  const stats = [
    { icon: FaUserMd, value: '300+', label: 'Expert Dentists' },
    { icon: FaClinicMedical, value: '120+', label: 'Dental Clinics' },
    { icon: FaTooth, value: '40K+', label: 'Smiles Restored' },
    { icon: FaShieldAlt, value: '100%', label: 'Secure Booking' },
  ]

  return (
    <section className="hero-image relative min-h-[90vh] flex items-center py-20 md:py-32 overflow-hidden">
      {/* Decorative Elements */}
      <div className="absolute top-20 left-10 w-64 h-64 bg-primary-400/10 rounded-full blur-3xl floating-element"></div>
      <div className="absolute bottom-20 right-10 w-80 h-80 bg-secondary-400/10 rounded-full blur-3xl floating-element" style={{ animationDelay: '2s' }}></div>
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[600px] h-[600px] bg-primary-300/5 rounded-full blur-3xl"></div>

      {/* Floating tooth icons */}
      <div className="absolute top-32 right-24 opacity-10 text-primary-400 text-6xl floating-element" style={{ animationDelay: '1s' }}>
        <FaTooth />
      </div>
      <div className="absolute bottom-40 left-24 opacity-10 text-secondary-400 text-5xl floating-element" style={{ animationDelay: '3s' }}>
        <FaTooth />
      </div>

      <div className="container mx-auto px-4 relative z-10">
        <div className="max-w-4xl mx-auto text-center">
          {/* Badge */}
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary-100/80 backdrop-blur-sm border border-primary-200/50 mb-8 animate-fade-in-down">
            <span className="w-2 h-2 rounded-full bg-primary-500 animate-pulse"></span>
            <span className="text-sm font-medium text-primary-700">Your Trusted Dental Care Companion</span>
          </div>

          {/* Main Heading */}
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-display font-bold text-neutral-900 mb-6 leading-tight animate-fade-in-up">
            Exceptional Dental Care
            <br />
            <span className="gradient-text">For Every Smile</span>
          </h1>

          {/* Subtitle */}
          <p className="text-lg md:text-xl text-neutral-600 mb-10 max-w-2xl mx-auto leading-relaxed animate-fade-in-up" style={{ animationDelay: '0.1s' }}>
            Connect with verified dental professionals and clinics near you.
            Book appointments for cleanings, orthodontics, implants & more —
            all in one place with <span className="font-semibold text-primary-600">DentalCare+</span>.
          </p>

          {/* CTA Buttons */}
          <div className="flex flex-col sm:flex-row gap-4 justify-center mb-16 animate-fade-in-up" style={{ animationDelay: '0.2s' }}>
            <button
              onClick={openAuthModal}
              className="btn-primary text-lg px-8 py-4 flex items-center justify-center gap-2 group"
            >
              Book an Appointment
              <FaArrowRight className="transition-transform duration-300 group-hover:translate-x-1" />
            </button>
            <a
              href="#how-it-works"
              className="btn-secondary text-lg px-8 py-4"
            >
              How It Works
            </a>
          </div>

          {/* Stats */}
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6 max-w-3xl mx-auto animate-fade-in-up" style={{ animationDelay: '0.3s' }}>
            {stats.map(({ icon: Icon, value, label }, index) => (
              <div
                key={label}
                className="glass-card p-4 text-center stat-item"
                style={{ animationDelay: `${0.3 + index * 0.1}s` }}
              >
                <div className="icon-container w-12 h-12 mx-auto mb-3">
                  <Icon className="text-primary-600 text-xl" />
                </div>
                <div className="text-2xl md:text-3xl font-bold text-neutral-900">{value}</div>
                <div className="text-sm text-neutral-500">{label}</div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Bottom gradient fade */}
      <div className="absolute bottom-0 left-0 right-0 h-24 bg-gradient-to-t from-white to-transparent"></div>
    </section>
  )
}

export default Hero
