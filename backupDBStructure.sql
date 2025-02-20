-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 31-03-2021 a las 11:23:13
-- Versión del servidor: 10.4.13-MariaDB
-- Versión de PHP: 7.2.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `springjpa`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databasefield`
--

CREATE TABLE `databasefield` (
  `id` bigint(20) NOT NULL,
  `database_table_id` bigint(20) NOT NULL,
  `field_name` varchar(50) NOT NULL,
  `field_type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databaseinfo`
--

CREATE TABLE `databaseinfo` (
  `id` bigint(20) NOT NULL,
  `database_name` varchar(50) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databaserelation`
--

CREATE TABLE `databaserelation` (
  `id` bigint(20) NOT NULL,
  `database_field_id` bigint(20) NOT NULL,
  `database_referenced_field_id` bigint(20) NOT NULL,
  `database_table_id` bigint(20) NOT NULL,
  `database_referenced_table_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `databasetable`
--

CREATE TABLE `databasetable` (
  `id` bigint(20) NOT NULL,
  `database_id` bigint(20) NOT NULL,
  `table_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_MODERATOR'),
(3, 'ROLE_ADMIN');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_roles`
--

CREATE TABLE `user_roles` (
  `role_id` int(11) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `databasefield`
--
ALTER TABLE `databasefield`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_databasefield_databasetable` (`database_table_id`);

--
-- Indices de la tabla `databaseinfo`
--
ALTER TABLE `databaseinfo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_databaseinfo_users` (`user_id`);

--
-- Indices de la tabla `databaserelation`
--
ALTER TABLE `databaserelation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_databaserelation_databasetable1` (`database_table_id`),
  ADD KEY `FK_databaserelation_databasetable2` (`database_referenced_table_id`),
  ADD KEY `FK_databaserelation_databasefield3` (`database_field_id`),
  ADD KEY `FK_databaserelation_databasefield4` (`database_referenced_field_id`);

--
-- Indices de la tabla `databasetable`
--
ALTER TABLE `databasetable`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_databasetable_databaseinfo` (`database_id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `user_roles`
--
ALTER TABLE `user_roles`
  ADD KEY `FK_userroles_roles` (`role_id`),
  ADD KEY `FK_userroles_users` (`user_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `databasefield`
--
ALTER TABLE `databasefield`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `databaseinfo`
--
ALTER TABLE `databaseinfo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `databaserelation`
--
ALTER TABLE `databaserelation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `databasetable`
--
ALTER TABLE `databasetable`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `databasefield`
--
ALTER TABLE `databasefield`
  ADD CONSTRAINT `FK_databasefield_databasetable` FOREIGN KEY (`database_table_id`) REFERENCES `databasetable` (`id`);

--
-- Filtros para la tabla `databaseinfo`
--
ALTER TABLE `databaseinfo`
  ADD CONSTRAINT `FK_databaseinfo_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `databaserelation`
--
ALTER TABLE `databaserelation`
  ADD CONSTRAINT `FK_databaserelation_databasefield3` FOREIGN KEY (`database_field_id`) REFERENCES `databasefield` (`id`),
  ADD CONSTRAINT `FK_databaserelation_databasefield4` FOREIGN KEY (`database_referenced_field_id`) REFERENCES `databasefield` (`id`),
  ADD CONSTRAINT `FK_databaserelation_databasetable1` FOREIGN KEY (`database_table_id`) REFERENCES `databasetable` (`id`),
  ADD CONSTRAINT `FK_databaserelation_databasetable2` FOREIGN KEY (`database_referenced_table_id`) REFERENCES `databasetable` (`id`);

--
-- Filtros para la tabla `databasetable`
--
ALTER TABLE `databasetable`
  ADD CONSTRAINT `FK_databasetable_databaseinfo` FOREIGN KEY (`database_id`) REFERENCES `databaseinfo` (`id`);

--
-- Filtros para la tabla `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FK_userroles_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FK_userroles_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
