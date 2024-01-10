'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

const SettingsNavigation = () => {
  const pathname = usePathname();
  const navigation = [
    {
      name: 'Registration',
      href: '/admin/settings',
      current: pathname === '/admin/settings',
    },
  ];

  return (
    <header className="border-b border-white/5">
      {/* Secondary navigation */}
      <nav className="flex overflow-x-auto py-4">
        <ul
          role="list"
          className="flex min-w-full flex-none gap-x-6 px-4 text-sm font-semibold leading-6 text-gray-400 sm:px-6 lg:px-8"
        >
          {navigation.map((item) => (
            <li key={item.name}>
              <Link
                href={item.href}
                className={item.current ? 'text-indigo-400' : ''}
              >
                {item.name}
              </Link>
            </li>
          ))}
        </ul>
      </nav>
    </header>
  );
};

export default SettingsNavigation;
