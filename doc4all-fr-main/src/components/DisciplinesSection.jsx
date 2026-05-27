// DisciplinesSection.jsx - Dental Specialties Cards
import { FaArrowRight } from 'react-icons/fa'
import { MdCleaningServices } from 'react-icons/md'

// Using SVG icons inline for dental specialties not in react-icons
const ToothIcon = ({ className }) => (
  <svg className={className} viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 1C8.5 1 6 3.5 6 5.5c0 1 .3 2 .8 2.8L5.5 19c-.2 1 .5 2 1.5 2s1.8-.8 2-1.8L10 14h4l1 5.2c.2 1 1 1.8 2 1.8s1.7-1 1.5-2l-1.3-10.7c.5-.8.8-1.8.8-2.8C18 3.5 15.5 1 12 1z"/>
  </svg>
)

const BraceIcon = ({ className }) => (
  <svg className={className} viewBox="0 0 24 24" fill="currentColor">
    <path d="M4 6h2v2H4V6zm0 4h2v2H4v-2zm0 4h2v2H4v-2zm14-8h2v2h-2V6zm0 4h2v2h-2v-2zm0 4h2v2h-2v-2zM8 4h8v2H8V4zm0 14h8v2H8v-2zm0-7h8v2H8v-2z"/>
  </svg>
)

const ImplantIcon = ({ className }) => (
  <svg className={className} viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 2C9.24 2 7 4.24 7 7c0 1.8.9 3.38 2.27 4.35L8 20h8l-1.27-8.65C16.1 10.38 17 8.8 17 7c0-2.76-2.24-5-5-5zm0 2c1.65 0 3 1.35 3 3S13.65 10 12 10s-3-1.35-3-3 1.35-3 3-3zm-2.5 8h5l.9 6h-6.8l.9-6z"/>
  </svg>
)

const disciplines = [
  {
    IconComp: ToothIcon,
    title: "General Dentistry",
    description: "Comprehensive routine dental care including check-ups, cleanings, fillings, and preventive treatments to maintain your oral health and catch issues early.",
    benefits: ["Routine check-ups & X-rays", "Teeth cleaning & polishing", "Cavity fillings", "Gum disease prevention"],
    color: {
      bg: "from-sky-500 to-primary-600",
      light: "bg-sky-50",
      border: "border-sky-400",
      text: "text-sky-600",
      icon: "text-sky-500"
    },
    type: "general"
  },
  {
    IconComp: BraceIcon,
    title: "Orthodontics",
    description: "Straighten teeth and correct bite alignment with modern braces, clear aligners (Invisalign), and retainers tailored to children, teens, and adults.",
    benefits: ["Metal & ceramic braces", "Clear aligner therapy", "Bite correction", "Retainers & follow-up care"],
    color: {
      bg: "from-secondary-500 to-teal-600",
      light: "bg-teal-50",
      border: "border-teal-400",
      text: "text-teal-600",
      icon: "text-teal-500"
    },
    type: "orthodontics"
  },
  {
    IconComp: ImplantIcon,
    title: "Implants & Cosmetic",
    description: "Restore confidence with dental implants, veneers, whitening, and full smile makeovers using the latest cosmetic dentistry techniques.",
    benefits: ["Dental implants", "Porcelain veneers", "Teeth whitening", "Full smile makeovers"],
    color: {
      bg: "from-primary-600 to-blue-700",
      light: "bg-blue-50",
      border: "border-blue-400",
      text: "text-blue-600",
      icon: "text-blue-500"
    },
    type: "cosmetic"
  }
]

const DisciplinesSection = () => {
  return (
    <section className="py-20 md:py-28 bg-white relative overflow-hidden">
      {/* Background pattern */}
      <div className="absolute inset-0 mesh-bg opacity-50"></div>

      <div className="container mx-auto px-4 relative z-10">
        {/* Section Header */}
        <div className="text-center mb-16">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary-100 mb-4">
            <span className="text-sm font-semibold text-primary-700">Our Dental Specialties</span>
          </div>
          <h2 className="section-title mb-4">
            Choose Your
            <span className="gradient-text"> Dental Treatment</span>
          </h2>
          <p className="section-subtitle">
            DentalCare+ connects you with specialists across all dental disciplines,
            so you always get the right care for your smile.
          </p>
        </div>

        {/* Disciplines Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {disciplines.map((discipline, index) => (
            <div
              key={index}
              className={`discipline-card ${discipline.type} bg-white rounded-3xl shadow-card hover:shadow-card-hover transition-all duration-500 group overflow-hidden animate-fade-in-up`}
              style={{ animationDelay: `${index * 0.15}s` }}
            >
              {/* Top gradient bar */}
              <div className={`h-2 bg-gradient-to-r ${discipline.color.bg}`}></div>

              <div className="p-8">
                {/* Icon */}
                <div className={`w-20 h-20 rounded-3xl ${discipline.color.light} flex items-center justify-center mb-6 transition-all duration-300 group-hover:scale-110`}>
                  <discipline.IconComp className={`w-10 h-10 ${discipline.color.icon}`} />
                </div>

                {/* Title */}
                <h3 className="text-2xl font-bold text-neutral-900 mb-4 group-hover:text-primary-600 transition-colors">
                  {discipline.title}
                </h3>

                {/* Description */}
                <p className="text-neutral-600 leading-relaxed mb-6">
                  {discipline.description}
                </p>

                {/* Benefits */}
                <div className="mb-6">
                  <h4 className="font-semibold text-neutral-800 mb-3 flex items-center gap-2">
                    <span className={`w-1.5 h-1.5 rounded-full bg-gradient-to-r ${discipline.color.bg} inline-block`}></span>
                    Key Treatments
                  </h4>
                  <ul className="space-y-2">
                    {discipline.benefits.map((benefit, idx) => (
                      <li key={idx} className="flex items-center gap-3 text-neutral-600">
                        <svg className={`w-5 h-5 ${discipline.color.text} flex-shrink-0`} fill="currentColor" viewBox="0 0 20 20">
                          <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clipRule="evenodd" />
                        </svg>
                        <span>{benefit}</span>
                      </li>
                    ))}
                  </ul>
                </div>

                {/* CTA */}
                <button className={`flex items-center gap-2 ${discipline.color.text} font-semibold group/btn`}>
                  Find Dentists
                  <FaArrowRight className="text-sm transition-transform group-hover/btn:translate-x-1" />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

export default DisciplinesSection