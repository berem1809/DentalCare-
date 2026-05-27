// FeatureSection.jsx - DentalCare+ Feature Cards
import { FaSearch, FaBell, FaUserMd, FaStar, FaCreditCard, FaUserEdit, FaHistory, FaTooth } from 'react-icons/fa'
import { MdVerified, MdMedicalServices } from 'react-icons/md'

const FeatureSection = () => {
  const features = [
    {
      icon: FaSearch,
      title: "Smart Symptom Search",
      description: "Find the right dental specialist for toothache, braces, implants, or cosmetic work — instantly.",
      color: "from-primary-500 to-primary-600"
    },
    {
      icon: FaTooth,
      title: "All Dental Specialties",
      description: "Access General Dentistry, Orthodontics, Periodontics, Endodontics, and Cosmetic Dentistry.",
      color: "from-secondary-500 to-secondary-600"
    },
    {
      icon: FaBell,
      title: "Appointment Reminders",
      description: "Never miss a check-up. Get timely reminders for your upcoming dental visits and follow-ups.",
      color: "from-sky-500 to-primary-500"
    },
    {
      icon: MdVerified,
      title: "Verified Dentists",
      description: "All dental professionals are license-verified and credentialed for your safety and peace of mind.",
      color: "from-primary-500 to-secondary-500"
    },
    {
      icon: FaStar,
      title: "Patient Reviews",
      description: "Read real, anonymous patient reviews to pick the best dentist for your specific needs.",
      color: "from-yellow-400 to-orange-500"
    },
    {
      icon: FaCreditCard,
      title: "Secure Payments",
      description: "Book confidently with our encrypted payment system. Insurance-friendly billing support.",
      color: "from-secondary-500 to-primary-600"
    },
    {
      icon: FaUserEdit,
      title: "Dental Health Profile",
      description: "Manage your dental records, X-ray history, treatment plans, and preferences in one place.",
      color: "from-primary-600 to-primary-700"
    },
    {
      icon: FaHistory,
      title: "Treatment History",
      description: "Track past treatments, medications, and upcoming procedures across all your dental visits.",
      color: "from-secondary-600 to-teal-600"
    }
  ]

  return (
    <section className="py-20 md:py-28 bg-gradient-to-b from-white to-primary-50/40 relative overflow-hidden">
      {/* Background decoration */}
      <div className="absolute top-0 right-0 w-96 h-96 bg-primary-100/50 rounded-full blur-3xl -translate-y-1/2 translate-x-1/2"></div>
      <div className="absolute bottom-0 left-0 w-80 h-80 bg-secondary-100/50 rounded-full blur-3xl translate-y-1/2 -translate-x-1/2"></div>

      <div className="container mx-auto px-4 relative z-10">
        {/* Section Header */}
        <div className="text-center mb-16">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary-100 mb-4">
            <span className="text-sm font-semibold text-primary-700">Why Choose DentalCare+</span>
          </div>
          <h2 className="section-title mb-4">
            Everything You Need for
            <span className="gradient-text"> A Healthy Smile</span>
          </h2>
          <p className="section-subtitle">
            A complete digital platform designed around modern dental care — from booking to billing.
          </p>
        </div>

        {/* Feature Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 lg:gap-8">
          {features.map((feature, index) => (
            <div
              key={index}
              className="feature-card p-6 group animate-fade-in-up"
              style={{ animationDelay: `${index * 0.1}s` }}
            >
              {/* Icon */}
              <div className={`w-14 h-14 rounded-2xl bg-gradient-to-br ${feature.color} flex items-center justify-center mb-5 shadow-lg transition-all duration-300 group-hover:scale-110 group-hover:shadow-glow`}>
                <feature.icon className="text-white text-2xl" />
              </div>

              {/* Content */}
              <h3 className="text-xl font-semibold text-neutral-900 mb-3 group-hover:text-primary-600 transition-colors">
                {feature.title}
              </h3>
              <p className="text-neutral-600 leading-relaxed">
                {feature.description}
              </p>

              {/* Hover indicator */}
              <div className="mt-4 flex items-center text-primary-600 font-medium opacity-0 group-hover:opacity-100 transition-opacity duration-300">
                <span className="text-sm">Learn more</span>
                <svg className="w-4 h-4 ml-1 transform group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

export default FeatureSection