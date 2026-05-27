// Footer.jsx - DentalCare+ Footer
import { Link } from 'react-router-dom'
import { FaFacebook, FaTwitter, FaInstagram, FaLinkedin, FaTooth, FaEnvelope, FaPhone, FaMapMarkerAlt } from 'react-icons/fa'

const Footer = () => {
  const currentYear = new Date().getFullYear()

  const footerLinks = {
    company: [
      { label: 'About Us', href: '#' },
      { label: 'Our Mission', href: '#' },
      { label: 'Careers', href: '#' },
      { label: 'Press', href: '#' },
    ],
    services: [
      { label: 'Find Dentists', href: '/find-dispensaries' },
      { label: 'Browse Clinics', href: '/browse-dispensaries' },
      { label: 'Book Appointments', href: '#' },
      { label: 'Dental Resources', href: '#' },
    ],
    legal: [
      { label: 'Terms of Service', href: '#' },
      { label: 'Privacy Policy', href: '#' },
      { label: 'Cookie Policy', href: '#' },
      { label: 'HIPAA Compliance', href: '#' },
    ],
  }

  const socialLinks = [
    { icon: FaFacebook, href: '#', label: 'Facebook' },
    { icon: FaTwitter, href: '#', label: 'Twitter' },
    { icon: FaInstagram, href: '#', label: 'Instagram' },
    { icon: FaLinkedin, href: '#', label: 'LinkedIn' },
  ]

  return (
    <footer className="relative bg-neutral-900 text-white overflow-hidden">
      {/* Gradient overlay — dental blue tint */}
      <div className="absolute inset-0 bg-gradient-to-br from-primary-900/25 via-transparent to-secondary-900/20"></div>

      <div className="relative z-10">
        {/* Main footer content */}
        <div className="container mx-auto px-4 py-16">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-12">

            {/* Brand section */}
            <div className="lg:col-span-2">
              <Link to="/" className="flex items-center gap-2 mb-6 group">
                <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-primary-400 to-secondary-500 flex items-center justify-center transition-transform duration-300 group-hover:scale-110">
                  <FaTooth className="text-white text-xl" />
                </div>
                <span className="text-2xl font-display font-bold">
                  Dental<span className="text-primary-400">Care+</span>
                </span>
              </Link>
              <p className="text-neutral-400 mb-6 max-w-sm leading-relaxed">
                Connecting patients with trusted dental professionals and clinics.
                Your brighter smile journey starts here.
              </p>

              {/* Contact info */}
              <div className="space-y-3 mb-6">
                <a href="mailto:info@dentalcareplus.com" className="flex items-center gap-3 text-neutral-400 hover:text-primary-400 transition-colors">
                  <FaEnvelope className="text-primary-500" />
                  <span>info@dentalcareplus.com</span>
                </a>
                <a href="tel:+1234567890" className="flex items-center gap-3 text-neutral-400 hover:text-primary-400 transition-colors">
                  <FaPhone className="text-primary-500" />
                  <span>(123) 456-7890</span>
                </a>
                <div className="flex items-center gap-3 text-neutral-400">
                  <FaMapMarkerAlt className="text-primary-500" />
                  <span>123 Smile Street, Dental City</span>
                </div>
              </div>

              {/* Social links */}
              <div className="flex gap-3">
                {socialLinks.map(({ icon: Icon, href, label }) => (
                  <a
                    key={label}
                    href={href}
                    aria-label={label}
                    className="w-10 h-10 rounded-xl bg-neutral-800 hover:bg-primary-600 flex items-center justify-center transition-all duration-300 hover:-translate-y-1"
                  >
                    <Icon className="text-lg" />
                  </a>
                ))}
              </div>
            </div>

            {/* Company links */}
            <div>
              <h3 className="font-semibold text-lg mb-4 text-white">Company</h3>
              <ul className="space-y-3">
                {footerLinks.company.map(({ label, href }) => (
                  <li key={label}>
                    <a href={href} className="text-neutral-400 hover:text-primary-400 transition-colors animated-underline">
                      {label}
                    </a>
                  </li>
                ))}
              </ul>
            </div>

            {/* Services links */}
            <div>
              <h3 className="font-semibold text-lg mb-4 text-white">Services</h3>
              <ul className="space-y-3">
                {footerLinks.services.map(({ label, href }) => (
                  <li key={label}>
                    {href.startsWith('/') ? (
                      <Link to={href} className="text-neutral-400 hover:text-primary-400 transition-colors animated-underline">
                        {label}
                      </Link>
                    ) : (
                      <a href={href} className="text-neutral-400 hover:text-primary-400 transition-colors animated-underline">
                        {label}
                      </a>
                    )}
                  </li>
                ))}
              </ul>
            </div>

            {/* Legal links */}
            <div>
              <h3 className="font-semibold text-lg mb-4 text-white">Legal</h3>
              <ul className="space-y-3">
                {footerLinks.legal.map(({ label, href }) => (
                  <li key={label}>
                    <a href={href} className="text-neutral-400 hover:text-primary-400 transition-colors animated-underline">
                      {label}
                    </a>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>

        {/* Bottom bar */}
        <div className="border-t border-neutral-800">
          <div className="container mx-auto px-4 py-6">
            <div className="flex flex-col md:flex-row justify-between items-center gap-4">
              <p className="text-neutral-500 text-sm">
                © {currentYear} DentalCare+. All rights reserved.
              </p>
              <div className="flex items-center gap-2 text-sm text-neutral-500">
                <span>Crafted with</span>
                <FaTooth className="text-primary-500 animate-pulse-gentle" />
                <span>for healthier smiles</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </footer>
  )
}

export default Footer