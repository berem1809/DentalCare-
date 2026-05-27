// TestimonialsSection.jsx - Premium Testimonials Carousel
import { useState, useEffect } from 'react'
import { FaQuoteLeft, FaChevronLeft, FaChevronRight, FaStar, FaUserCircle } from 'react-icons/fa'

const TestimonialsSection = () => {
  const testimonials = [
    {
      quote: "DentalCare+ helped me find a brilliant orthodontist in minutes. The clear aligner treatment was recommended perfectly for my case. My smile is completely transformed!",
      name: "Sarah Johnson",
      role: "Patient",
      rating: 5,
      discipline: "Orthodontics",
      avatar: "SJ"
    },
    {
      quote: "As a dental implant specialist, this platform has transformed how I connect with patients. The booking system is seamless and my appointment schedule is always full.",
      name: "Dr. Michael Thompson",
      role: "Implant Specialist",
      rating: 5,
      discipline: "Cosmetic Dentistry",
      avatar: "MT"
    },
    {
      quote: "The advance booking system reduced no-shows at our dental clinic by over 70%. Our team can now focus on delivering quality care instead of chasing appointments.",
      name: "Dr. Priya Sharma",
      role: "General Dentist & Clinic Owner",
      rating: 5,
      discipline: "General Dentistry",
      avatar: "PS"
    },
    {
      quote: "I had severe dental anxiety but DentalCare+ helped me find a dentist who specialises in anxious patients. The reviews and ratings made the choice so much easier.",
      name: "Robert Lawrence",
      role: "Patient",
      rating: 5,
      discipline: "General Dentistry",
      avatar: "RL"
    },
    {
      quote: "Managing our multi-dentist clinic has never been simpler. DentalCare+ lets us organise specialists by discipline and handle patient bookings all from one dashboard.",
      name: "Lisa Chen",
      role: "Dental Clinic Manager",
      rating: 5,
      discipline: "Multi-Specialty Clinic",
      avatar: "LC"
    }
  ]

  const [currentIndex, setCurrentIndex] = useState(0)
  const [isAutoPlaying, setIsAutoPlaying] = useState(true)

  // Auto-play functionality
  useEffect(() => {
    if (!isAutoPlaying) return
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % testimonials.length)
    }, 5000)
    return () => clearInterval(interval)
  }, [isAutoPlaying, testimonials.length])

  const nextTestimonial = () => {
    setIsAutoPlaying(false)
    setCurrentIndex((prevIndex) => (prevIndex + 1) % testimonials.length)
  }

  const prevTestimonial = () => {
    setIsAutoPlaying(false)
    setCurrentIndex((prevIndex) => (prevIndex - 1 + testimonials.length) % testimonials.length)
  }

  const goToTestimonial = (index) => {
    setIsAutoPlaying(false)
    setCurrentIndex(index)
  }

  // Get visible testimonials for desktop (3 cards)
  const getVisibleTestimonials = () => {
    const visible = []
    for (let i = 0; i < 3; i++) {
      visible.push(testimonials[(currentIndex + i) % testimonials.length])
    }
    return visible
  }

  return (
    <section className="py-20 md:py-28 bg-gradient-to-b from-neutral-50 to-white relative overflow-hidden">
      {/* Background decoration */}
      <div className="absolute top-0 left-1/4 w-72 h-72 bg-primary-100/50 rounded-full blur-3xl"></div>
      <div className="absolute bottom-0 right-1/4 w-96 h-96 bg-secondary-100/50 rounded-full blur-3xl"></div>

      <div className="container mx-auto px-4 relative z-10">
        {/* Section Header */}
        <div className="text-center mb-16">
          <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-accent-100 mb-4">
            <span className="text-sm font-semibold text-accent-700">Testimonials</span>
          </div>
          <h2 className="section-title mb-4">
            Loved by
            <span className="gradient-text"> Thousands</span>
          </h2>
          <p className="section-subtitle">
            Discover how DentalCare+ is transforming dental experiences for patients and practitioners alike.
          </p>
        </div>

        {/* Desktop Testimonials - 3 cards */}
        <div className="hidden md:block max-w-6xl mx-auto">
          <div className="grid grid-cols-3 gap-6">
            {getVisibleTestimonials().map((testimonial, index) => (
              <div
                key={`${testimonial.name}-${index}`}
                className={`testimonial-card p-6 transition-all duration-500 ${index === 1 ? 'scale-105 shadow-card-hover' : 'opacity-90'
                  }`}
              >
                {/* Quote icon */}
                <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-primary-100 to-primary-50 flex items-center justify-center mb-4">
                  <FaQuoteLeft className="text-primary-500 text-lg" />
                </div>

                {/* Quote text */}
                <p className="text-neutral-700 leading-relaxed mb-6 italic">
                  "{testimonial.quote}"
                </p>

                {/* Rating */}
                <div className="flex gap-1 mb-4">
                  {[...Array(5)].map((_, i) => (
                    <FaStar
                      key={i}
                      className={`text-sm ${i < testimonial.rating ? 'text-yellow-400' : 'text-neutral-200'}`}
                    />
                  ))}
                </div>

                {/* Author info */}
                <div className="flex items-center gap-3">
                  <div className="w-12 h-12 rounded-full bg-gradient-to-br from-primary-500 to-secondary-500 flex items-center justify-center text-white font-semibold">
                    {testimonial.avatar}
                  </div>
                  <div>
                    <p className="font-semibold text-neutral-900">{testimonial.name}</p>
                    <p className="text-sm text-neutral-500">{testimonial.role}</p>
                    <p className="text-xs text-primary-500 font-medium">{testimonial.discipline}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Mobile Testimonial - Single card */}
        <div className="md:hidden">
          <div className="testimonial-card p-6 max-w-md mx-auto">
            {/* Quote icon */}
            <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-primary-100 to-primary-50 flex items-center justify-center mb-4">
              <FaQuoteLeft className="text-primary-500 text-lg" />
            </div>

            {/* Quote text */}
            <p className="text-neutral-700 leading-relaxed mb-6 italic">
              "{testimonials[currentIndex].quote}"
            </p>

            {/* Rating */}
            <div className="flex gap-1 mb-4">
              {[...Array(5)].map((_, i) => (
                <FaStar
                  key={i}
                  className={`text-sm ${i < testimonials[currentIndex].rating ? 'text-yellow-400' : 'text-neutral-200'}`}
                />
              ))}
            </div>

            {/* Author info */}
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 rounded-full bg-gradient-to-br from-primary-500 to-secondary-500 flex items-center justify-center text-white font-semibold">
                {testimonials[currentIndex].avatar}
              </div>
              <div>
                <p className="font-semibold text-neutral-900">{testimonials[currentIndex].name}</p>
                <p className="text-sm text-neutral-500">{testimonials[currentIndex].role}</p>
                <p className="text-xs text-primary-500 font-medium">{testimonials[currentIndex].discipline}</p>
              </div>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <div className="flex items-center justify-center gap-6 mt-10">
          {/* Prev button */}
          <button
            onClick={prevTestimonial}
            className="w-12 h-12 rounded-xl bg-white shadow-card hover:shadow-card-hover flex items-center justify-center transition-all duration-300 hover:-translate-x-1 group"
            aria-label="Previous testimonial"
          >
            <FaChevronLeft className="text-neutral-600 group-hover:text-primary-600" />
          </button>

          {/* Dots */}
          <div className="flex gap-2">
            {testimonials.map((_, index) => (
              <button
                key={index}
                onClick={() => goToTestimonial(index)}
                className={`h-2 rounded-full transition-all duration-300 ${index === currentIndex
                    ? 'w-8 bg-primary-500'
                    : 'w-2 bg-neutral-300 hover:bg-neutral-400'
                  }`}
                aria-label={`Go to testimonial ${index + 1}`}
              />
            ))}
          </div>

          {/* Next button */}
          <button
            onClick={nextTestimonial}
            className="w-12 h-12 rounded-xl bg-white shadow-card hover:shadow-card-hover flex items-center justify-center transition-all duration-300 hover:translate-x-1 group"
            aria-label="Next testimonial"
          >
            <FaChevronRight className="text-neutral-600 group-hover:text-primary-600" />
          </button>
        </div>
      </div>
    </section>
  )
}

export default TestimonialsSection