package display;

import bot.message.MessageFormatter;
import bot.models.ListedTask;
import bot.models.TaskListing;
import domain.task.Task;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.Language;

import static display.FormattingUtils.sanitize;

public class FrenchMessageFormatter implements MessageFormatter {
    @Override
    public String formatTaskTriggeredMessage(Task task) {
        return sanitize(task.getName());
    }

    @Override
    public String formatDefaultMessage() {
        return "Utilisez /help pour de l'information sur comment utiliser ce bot.";
    }

    @Override
    public String formatStartMessage() {
        return "Bienvenue ! Utilisez /help pour de l'information sur comment utiliser ce bot.";
    }

    @Override
    public String formatUnknownCommandMessage() {
        return "Commande inconnue. Utilisez /help pour une liste des commandes.";
    }

    @Override
    public String formatNoOngoingOperationMessage() {
        return "Il n'y a pas d'opération à annuler.";
    }

    @Override
    public String formatOperationCancelledMessage() {
        return "Operation annulée.";
    }

    @Override
    public String formatHelpMessage() {
        return "CrontaskBot vous permet de planifier des rappels.\n\n"
            + "/task \u2014 Créer une nouvelle tâche\n"
            + "/tasks \u2014 Gérer vos tâches planifiées\n"
            + "/timezone \u2014 Changer les paramètres de fuseau horaire\n"
            + "/help \u2014 Afficher ce message d'information\n"
            + "/cancel \u2014 Annuler l'opération en cours\n"
            + "\n"
            + "Vous pouvez planifier des rappels pour en suivant le format cron d'Unix. Utilisez <a href=\"https://crontab.guru/\">ce site web</a> pour plus d'information sur la syntaxe cron.\n\n"
            + "Vous pouvez aussi planifier un rappel unique en fournissant une date ou un moment à la minute près, e.g. <i>2020-03-25 16:05</i>, <i>16:05</i> ou simplement <i>2020-03-25</i>.\n\n"
            + "De plus, vous pouvez écrire  \"in 5 minutes\" ou \"in 3 days and 5 hours\" (en anglais seulement) pour planifier un rappel dans le futur sans le temps exact.\n"
            + "\n"
            + "Ce bot vérifie les rappels à chaque minute, donc les notifications peuvent être en retard ou en avance jusqu'à 30 secondes.\n"
            + "\n"
            + "Code source disponible <a href=\"https://github.com/DomJob/CrontaskBot/\">ici</a>";
    }

    @Override
    public String formatTaskNameRequestMessage() {
        return "Entrez le nom de la tâche.";
    }

    @Override
    public String formatInvalidScheduleFormat() {
        return "Format invalide, veuillez réessayer. Ou utiliser /help pour plus d'information à propos du format des horaires";
    }

    @Override
    public String formatTaskCreatedMessage() {
        return "Tâche créée avec succès !";
    }

    @Override
    public String formatScheduleRequestedMessage() {
        return "Entrez l'horaire de cette tâche. Elle peut suivre le format cron, ou elle peut être un temps exact.";
    }

    @Override
    public String formatTimezoneOffsetRequestedMessage(Timezone currentTimezone, Time now) {
        return String.format("Entrez le décalage horaire par rapport à UTC de votre fuseau horaire dans le format \u00b1HH:MM .\n\nPour référence, il est présentement <b>%d:%d</b> dans le fuseau UTC.\n\nVotre décalage horaire est présentement réglé à <b>%s</b>.", now.hour(), now.minute(), currentTimezone.toString());
    }

    @Override
    public String formatTimezoneSetMessage() {
        return "Fuseau horaire réglé avec succès !";
    }

    @Override
    public String formatInvalidTimezoneMessage() {
        return "Décalage invalide, veuillez réessayer.";
    }

    @Override
    public String formatInvalidCommand() {
        return "Commande invalide.";
    }

    @Override
    public String formatTaskListingMessage(TaskListing listing) {
        StringBuilder message = new StringBuilder(String.format("Tâches <b>%d\u2014%d</b> de <b>%d</b>\n\n", listing.getStart() + 1, listing.getEnd(), listing.size()));

        for (ListedTask task : listing.getPage()) {
            String name = task.name;
            if (name.length() > 15) {
                name = name.substring(0, 15) + "...";
            }
            message.append(String.format("<b>%d</b>. %s\n", task.index, sanitize(name)));
            message.append(String.format("Prévue pour <b>%s</b>\n\n", task.scheduledFor));
        }

        boolean previous = listing.hasPreviousPage();
        boolean next = listing.hasNextPage();

        message.append("Utilisez /delete suivie du nombre de la tâche que vous souhaitez annuler.\n");

        if (previous && next) {
            message.append("\nUtilisez /previous ou /next pour voir plus de tâches.");
        } else if (previous) {
            message.append("\nUtilisez /previous pour voir plus de tâches.");
        } else if (next) {
            message.append("\nUtilisez /next pour voir plus de tâches.");
        }

        return message.toString();
    }

    @Override
    public String formatNoTasksMessage() {
        return "Vous n'avez présentement aucune tâche.";
    }

    @Override
    public String formatTaskDeletedMessage() {
        return "Tâche supprimée.";
    }

    @Override
    public String formatInvalidCommandDuringListing() {
        return "Commande invalide. Utilisez /cancel pour quitter l'affichage de la liste.";
    }

    @Override
    public String formatInvalidDeleteCommand() {
        return "Format invalide. \u2014 Veuillez sélectionner un nombre valide dans la liste ci-dessus.";
    }

    @Override
    public String formatLanguageInformationMessage() {
        StringBuilder sb = new StringBuilder("Utilisez /language suivi du code à deux lettres du langage désiré.\n\nActuellement, les langages suivant sont supportés:\n\n");

        for(Language langage : Language.values()) {
            sb.append(String.format(" \u2014 %s (<b>%s</b>)\n", langage.getDisplayName(), langage.getCode()));
        }

        return sb.toString();
    }

    @Override
    public String formatInvalidLangageMessage() {
        return "Code de langage invalide. Utilisez /language pour voir la liste des langages supportés.";
    }

    @Override
    public String formatLanguageSetMessage() {
        return "Langage modifié avec succès !";
    }
}
