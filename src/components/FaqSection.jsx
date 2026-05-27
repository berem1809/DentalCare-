// FaqSection.jsx - Premium FAQ Accordion
import { useState } from 'react'
import { FaChevronDown, FaQuestionCircle } from 'react-icons/fa'

const FaqSection = () => {
  const faqs = [
    {
      question: "How does the dentist search work?",
      answer: "Our smart search lets you find dentists by specialty (general, orthodontics, implants, cosmetic), location, language, ratings, and availability. You can also search by symptoms — like tooth pain or sensitivity — and we'll suggest the most relevant specialists."
    },
    {
      question: "What dental specialties are available on DentalCare+?",
      answer: "DentalCare+ covers the full spectrum of dental care: General Dentistry (check-ups, fillings, extractions), Orthodontics (braces, Invisalign), Cosmetic Dentistry (veneers, whitening, smile makeovers), Periodontics (gum treatment), Endodontics (root canals), and Oral Surgery (implants, wisdom teeth)."
    },
    {
      question: "How does the advance booking payment work?",
      answer: "When you book an appointment, a small advance deposit (typically 10–20% of the consultation fee) is charged to confirm your slot. This reduces no-shows. The remaining balance is paid at the clinic. All transactions are encrypted and secure. Some clinics also support direct insurance billing."
    },
    {
      question: "Can I store my dental X-rays and records on the platform?",
      answer: "Yes! DentalCare+ provides a secure dental health profile where you can upload X-rays, treatment history, prescriptions, and notes from past visits. Your dentist can access these with your permission, saving time and improving the quality of care."
    },
    {
      question: "How do I leave a review for my dentist?",
      answer: "After each appointment, you'll receive a prompt to rate your experience. Reviews are published anonymously to protect your privacy. You can rate aspects like wait time, clinic cleanliness, dentist communication, and overall treatment quality."
    },
    {
      question: "Can dental clinics register multiple dentists?",
      answer: "Yes! Dental clinics can add multiple practitioners under one clinic profile. Each dentist has their own specialty, schedule, bio, and patient reviews. Clinic managers can manage bookings, approve appointments, and view analytics from a central dashboard."
    },
    {
      question: "Is my dental and personal data secure?",
      answer: "Absolutely. We use industry-standard 256-bit AES encryption, secure payment processing, and comply with HIPAA and regional healthcare data protection laws. Your dental records and personal information are never shared with third parties without your explicit consent."
    },
    {
      question: "How can I cancel or reschedule a dental appointment?",
      answer: "You can manage all your appointments from your personal dashboard. Free cancellation is available up to 24 hours before the scheduled appointment. Rescheduling is done directly by selecting a new available time slot. Individual clinic cancellation policies may apply."
    }
  ]

  const [openIndex, setOpenIndex] = useState(null)

  const toggleFaq = (index) => {
    setOpenIndex(openIndex === index ? null : index)
  }

  return (
    <section className="py-20 md:py-28 bg-white relative overflow-hidden">
      {/* Background decoration */}
      <div className="absolute top-20 right-0 w-80 h-80 bg-primary-100/30 rounded-full blur-3xl"></div>
      <div className="absolute bottom-20 left-0 w-96 h-96 bg-secondary-100/30 rounded-full blur-3xl"></div>

      <div className="container mx-auto px-4 relative z-10">
        {/* Section Header */}
        <div className="text-center mb-16">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary-100 mb-4">
            <FaQuestionCircle className="text-primary-600" />
            <span className="text-sm font-semibold text-primary-700">FAQ</span>
          </div>
          <h2 className="section-title mb-4">
            Frequently Asked
            <span className="gradient-text"> Questions</span>
          </h2>
          <p className="section-subtitle">
            Find answers to common questions about using DentalCare+ for all your dental care needs.
          </p>
        </div>

        {/* FAQ List */}
        <div className="max-w-3xl mx-auto space-y-4">
          {faqs.map((faq, index) => (
            <div
              key={index}
              className={`faq-item bg-white border border-neutral-200 rounded-2xl overflow-hidden transition-all duration-300 animate-fade-in-up ${openIndex === index ? 'shadow-card-hover border-primary-200' : 'shadow-card hover:shadow-card-hover'
                }`}
              style={{ animationDelay: `${index * 0.05}s` }}
            >
              <button
                className={`faq-button w-full text-left p-6 flex justify-between items-start gap-4 transition-colors duration-200 ${openIndex === index ? 'bg-primary-50/50' : 'hover:bg-neutral-50'
                  }`}
                onClick={() => toggleFaq(index)}
                aria-expanded={openIndex === index}
              >
                <span className="font-semibold text-lg text-neutral-900 pr-4">
                  {faq.question}
                </span>
                <div className={`flex-shrink-0 w-8 h-8 rounded-full flex items-center justify-center transition-all duration-300 ${openIndex === index
                    ? 'bg-primary-500 text-white rotate-180'
                    : 'bg-neutral-100 text-neutral-600'
                  }`}>
                  <FaChevronDown className="text-sm" />
                </div>
              </button>

              {/* Answer - expandable */}
              <div
                className={`overflow-hidden transition-all duration-300 ${openIndex === index ? 'max-h-96' : 'max-h-0'
                  }`}
              >
                <div className="px-6 pb-6 pt-2">
                  <p className="text-neutral-600 leading-relaxed">
                    {faq.answer}
                  </p>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Contact CTA */}
        <div className="text-center mt-12">
          <p className="text-neutral-600 mb-4">Still have questions?</p>
          <button className="btn-secondary">
            Contact Support
          </button>
        </div>
      </div>
    </section>
  )
}

export default FaqSection