// HowItWorksSection.jsx - DentalCare+ Steps Section
import { FaUserPlus, FaSearch, FaCalendarCheck, FaCreditCard, FaTooth, FaCommentDots } from 'react-icons/fa'

const steps = [
  { icon: FaUserPlus, title: "Create Your Profile", description: "Sign up in minutes, add your dental history, insurance details, and treatment preferences." },
  { icon: FaSearch, title: "Find a Dentist", description: "Search by specialty, location, language, or availability — general dentist to orthodontist." },
  { icon: FaCalendarCheck, title: "Book an Appointment", description: "Pick a convenient time slot from the dentist's live availability calendar." },
  { icon: FaCreditCard, title: "Secure Payment", description: "Confirm your booking with a small deposit through our encrypted, insurance-friendly system." },
  { icon: FaTooth, title: "Visit the Clinic", description: "Walk in with all your details pre-filled. Enjoy a smooth, waiting-room-ready experience." },
  { icon: FaCommentDots, title: "Share Your Feedback", description: "Rate your dentist and help thousands of others find trusted dental care near them." }
]

const HowItWorksSection = () => {
  return (
    <section id="how-it-works" className="py-20 md:py-28 relative overflow-hidden">
      <div className="absolute inset-0 bg-gradient-to-br from-primary-600 via-primary-700 to-secondary-700"></div>
      <div className="absolute top-1/4 left-0 w-96 h-96 bg-white/5 rounded-full blur-3xl"></div>
      <div className="absolute bottom-1/4 right-0 w-[500px] h-[500px] bg-secondary-400/10 rounded-full blur-3xl"></div>
      <div className="absolute top-10 right-10 w-20 h-20 border border-white/20 rounded-full"></div>
      <div className="absolute bottom-20 left-20 w-32 h-32 border border-white/10 rounded-full"></div>
      <div className="absolute top-16 left-16 opacity-10 text-white text-5xl floating-element"><FaTooth /></div>
      <div className="absolute bottom-16 right-16 opacity-10 text-white text-6xl floating-element" style={{ animationDelay: '2s' }}><FaTooth /></div>

      <div className="container mx-auto px-4 relative z-10">
        <div className="text-center mb-16">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-white/10 backdrop-blur-sm border border-white/20 mb-4">
            <span className="text-sm font-semibold text-white">Simple Process</span>
          </div>
          <h2 className="text-3xl md:text-4xl font-display font-bold text-white mb-4">
            Your Perfect Smile in
            <span className="text-primary-200"> 6 Easy Steps</span>
          </h2>
          <p className="text-xl text-white/80 max-w-3xl mx-auto">
            Getting quality dental care has never been easier. Follow these steps to connect with the right dentist for you.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 max-w-6xl mx-auto">
          {steps.map((step, index) => (
            <div key={index} className="relative group animate-fade-in-up" style={{ animationDelay: `${index * 0.1}s` }}>
              <div className="bg-white/10 backdrop-blur-md border border-white/20 rounded-2xl p-6 h-full transition-all duration-300 hover:bg-white/15 hover:border-white/30 hover:-translate-y-1">
                <div className="flex items-center gap-4 mb-4">
                  <div className="flex-shrink-0 w-10 h-10 rounded-xl bg-gradient-to-br from-white/20 to-white/10 flex items-center justify-center">
                    <span className="text-lg font-bold text-white">{index + 1}</span>
                  </div>
                  <div className="w-12 h-12 rounded-xl bg-white/20 flex items-center justify-center transition-all duration-300 group-hover:scale-110 group-hover:bg-white/30">
                    <step.icon className="text-white text-xl" />
                  </div>
                </div>
                <h3 className="text-xl font-semibold text-white mb-2">{step.title}</h3>
                <p className="text-white/70 leading-relaxed">{step.description}</p>
              </div>
              {(index + 1) % 3 !== 0 && index !== steps.length - 1 && (
                <div className="hidden lg:block absolute top-1/2 -right-3 w-6 h-px bg-gradient-to-r from-white/40 to-transparent"></div>
              )}
            </div>
          ))}
        </div>

        <div className="text-center mt-12">
          <button className="bg-white text-primary-600 font-semibold px-8 py-4 rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 hover:-translate-y-1 hover:bg-primary-50">
            Book Your First Appointment
          </button>
        </div>
      </div>
    </section>
  )
}

export default HowItWorksSection