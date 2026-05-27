// BrowseDispensariesPage.jsx - Premium Browse Page
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FaSearch, FaLeaf, FaYinYang, FaHospital, FaStar, FaMapMarkerAlt, FaArrowRight, FaFilter } from 'react-icons/fa'

// Mock data 
const mockDispensaries = [
  {
    id: 1,
    name: "Wellness Ayurveda Center",
    location: "123 Kahatwoita St, Nittambuwa, WP",
    discipline: "ayurvedic",
    rating: 4.8,
    reviewCount: 124,
    image: "https://images.unsplash.com/photo-1731597076108-f3bbe268162f?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80",
    recommended: true,
    featured: true
  },
  {
    id: 2,
    name: "Homeopathic Harmony",
    location: "456 Nawala Way, Colombo, WP",
    discipline: "homeopathic",
    rating: 4.5,
    reviewCount: 89,
    image: "https://images.unsplash.com/photo-1512867957657-38dbae50a35b?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80",
    recommended: true,
    featured: false
  },
  {
    id: 3,
    name: "City Medical Center",
    location: "789 Bogaha Road, Colombo 8, WP",
    discipline: "western",
    rating: 4.7,
    reviewCount: 215,
    image: "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80",
    recommended: false,
    featured: true
  },
]

const DispensaryCard = ({ dispensary }) => {
  const navigate = useNavigate()

  const getDisciplineInfo = (discipline) => {
    switch (discipline) {
      case 'ayurvedic':
        return { icon: FaLeaf, color: 'text-green-600', bg: 'bg-green-100', name: 'Ayurvedic' }
      case 'homeopathic':
        return { icon: FaYinYang, color: 'text-blue-600', bg: 'bg-blue-100', name: 'Homeopathic' }
      case 'western':
        return { icon: FaHospital, color: 'text-rose-600', bg: 'bg-rose-100', name: 'Western' }
      default:
        return { icon: FaHospital, color: 'text-neutral-600', bg: 'bg-neutral-100', name: 'Unknown' }
    }
  }

  const disciplineInfo = getDisciplineInfo(dispensary.discipline)
  const DisciplineIcon = disciplineInfo.icon

  return (
    <div
      className="premium-card overflow-hidden cursor-pointer group"
      onClick={() => navigate(`/dispensary/${dispensary.id}`)}
    >
      {/* Image container */}
      <div className="relative h-48 overflow-hidden">
        <img
          src={dispensary.image}
          alt={dispensary.name}
          className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
        />
        {/* Gradient overlay */}
        <div className="absolute inset-0 bg-gradient-to-t from-black/40 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>

        {/* Rating badge */}
        <div className="absolute top-3 right-3 bg-white/95 backdrop-blur-sm px-3 py-1.5 rounded-full text-sm font-semibold flex items-center gap-1 shadow-card">
          <FaStar className="text-yellow-400" />
          <span className="text-neutral-800">{dispensary.rating}</span>
        </div>

        {/* Featured badge */}
        {dispensary.featured && (
          <div className="absolute top-3 left-3 bg-gradient-to-r from-primary-500 to-secondary-500 text-white px-3 py-1 rounded-full text-xs font-semibold shadow-glow">
            Featured
          </div>
        )}
      </div>

      {/* Content */}
      <div className="p-5">
        <h3 className="text-lg font-semibold text-neutral-900 mb-2 group-hover:text-primary-600 transition-colors line-clamp-1">
          {dispensary.name}
        </h3>

        {/* Location */}
        <div className="flex items-center text-sm text-neutral-500 mb-3">
          <FaMapMarkerAlt className="mr-2 text-primary-500 flex-shrink-0" />
          <span className="line-clamp-1">{dispensary.location}</span>
        </div>

        {/* Bottom row */}
        <div className="flex items-center justify-between pt-3 border-t border-neutral-100">
          <div className={`flex items-center gap-2 px-3 py-1.5 rounded-full ${disciplineInfo.bg}`}>
            <DisciplineIcon className={`text-sm ${disciplineInfo.color}`} />
            <span className={`text-xs font-medium ${disciplineInfo.color}`}>{disciplineInfo.name}</span>
          </div>
          <span className="text-sm text-neutral-400">{dispensary.reviewCount} reviews</span>
        </div>
      </div>
    </div>
  )
}

const BrowseDispensariesPage = () => {
  const navigate = useNavigate()
  const [searchQuery, setSearchQuery] = useState('')
  const [activeFilter, setActiveFilter] = useState('all')

  const handleSearch = (e) => {
    e.preventDefault()
    if (searchQuery.trim()) {
      navigate(`/search-dispensaries?q=${encodeURIComponent(searchQuery)}`)
    }
  }

  const filters = [
    { id: 'all', label: 'All', icon: null },
    { id: 'ayurvedic', label: 'Ayurvedic', icon: FaLeaf, color: 'text-green-600' },
    { id: 'homeopathic', label: 'Homeopathic', icon: FaYinYang, color: 'text-blue-600' },
    { id: 'western', label: 'Western', icon: FaHospital, color: 'text-rose-600' },
  ]

  const recommendedDispensaries = mockDispensaries.filter(d => d.recommended)
  const filteredDispensaries = activeFilter === 'all'
    ? mockDispensaries
    : mockDispensaries.filter(d => d.discipline === activeFilter)

  return (
    <div className="min-h-screen bg-gradient-to-b from-white to-neutral-50">
      {/* Hero Section */}
      <section className="py-12 md:py-16 bg-gradient-to-br from-primary-50 via-white to-secondary-50 relative overflow-hidden">
        <div className="absolute top-0 right-0 w-72 h-72 bg-primary-200/30 rounded-full blur-3xl"></div>
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-secondary-200/20 rounded-full blur-3xl"></div>

        <div className="container mx-auto px-4 relative z-10">
          <div className="max-w-3xl mx-auto text-center mb-10">
            <h1 className="text-3xl md:text-4xl font-display font-bold text-neutral-900 mb-4">
              Explore <span className="gradient-text">Wellness Centers</span>
            </h1>
            <p className="text-lg text-neutral-600">
              Discover trusted healthcare providers and wellness centers near you
            </p>
          </div>

          {/* Search Bar */}
          <div className="max-w-2xl mx-auto">
            <form onSubmit={handleSearch} className="relative">
              <div className="relative flex items-center glass-card p-2">
                <FaSearch className="absolute left-5 text-neutral-400" />
                <input
                  type="text"
                  placeholder="Search by name, location, or specialty..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-12 pr-4 py-3 bg-transparent border-none focus:outline-none text-neutral-700 placeholder:text-neutral-400"
                />
                <button
                  type="submit"
                  className="btn-primary px-6 py-3 flex items-center gap-2"
                >
                  <span className="hidden sm:inline">Search</span>
                  <FaArrowRight className="text-sm" />
                </button>
              </div>
            </form>
          </div>
        </div>
      </section>

      <div className="container mx-auto px-4 py-12">
        {/* Filter Pills */}
        <div className="flex flex-wrap gap-3 mb-10">
          <div className="flex items-center gap-2 text-neutral-500 mr-4">
            <FaFilter className="text-sm" />
            <span className="text-sm font-medium">Filter:</span>
          </div>
          {filters.map(filter => (
            <button
              key={filter.id}
              onClick={() => setActiveFilter(filter.id)}
              className={`flex items-center gap-2 px-4 py-2 rounded-full text-sm font-medium transition-all duration-200 ${activeFilter === filter.id
                  ? 'bg-primary-500 text-white shadow-glow'
                  : 'bg-white text-neutral-600 border border-neutral-200 hover:border-primary-300 hover:bg-primary-50'
                }`}
            >
              {filter.icon && <filter.icon className={activeFilter === filter.id ? 'text-white' : filter.color} />}
              {filter.label}
            </button>
          ))}
        </div>

        {/* Recommended Section */}
        {activeFilter === 'all' && (
          <section className="mb-14">
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-2xl font-display font-bold text-neutral-900">
                Recommended for You
              </h2>
              <button className="text-primary-600 font-medium flex items-center gap-1 hover:gap-2 transition-all animated-underline">
                View All <FaArrowRight className="text-sm" />
              </button>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {recommendedDispensaries.map(dispensary => (
                <DispensaryCard key={dispensary.id} dispensary={dispensary} />
              ))}
            </div>
          </section>
        )}

        {/* All Dispensaries */}
        <section>
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-display font-bold text-neutral-900">
              {activeFilter === 'all' ? 'All Wellness Centers' : `${filters.find(f => f.id === activeFilter)?.label} Centers`}
            </h2>
            <span className="text-neutral-500 text-sm">{filteredDispensaries.length} results</span>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredDispensaries.map(dispensary => (
              <DispensaryCard key={dispensary.id} dispensary={dispensary} />
            ))}
          </div>

          {filteredDispensaries.length === 0 && (
            <div className="text-center py-16">
              <div className="w-20 h-20 rounded-full bg-neutral-100 flex items-center justify-center mx-auto mb-4">
                <FaSearch className="text-3xl text-neutral-400" />
              </div>
              <h3 className="text-xl font-semibold text-neutral-700 mb-2">No results found</h3>
              <p className="text-neutral-500">Try adjusting your filters or search terms</p>
            </div>
          )}
        </section>
      </div>
    </div>
  )
}

export default BrowseDispensariesPage